package co.sentinel.cosmos

import android.util.Base64
import co.sentinel.cosmos.base.BaseChain
import co.sentinel.cosmos.base.BaseConstant.DENOM_DVPN
import co.sentinel.cosmos.base.BaseCosmosApp
import co.sentinel.cosmos.core.exception.WalletTaskError
import co.sentinel.cosmos.core.store.PrefsStore
import co.sentinel.cosmos.core.util.areValidKeywords
import co.sentinel.cosmos.cosmos.Signer
import co.sentinel.cosmos.crypto.CryptoHelper
import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.model.type.Coin
import co.sentinel.cosmos.model.type.Fee
import co.sentinel.cosmos.task.UserTask.GenerateAccountTask
import co.sentinel.cosmos.task.UserTask.GenerateSentinelAccountTask
import co.sentinel.cosmos.task.gRpcTask.AllRewardGrpcTask
import co.sentinel.cosmos.task.gRpcTask.AuthGrpcTask
import co.sentinel.cosmos.task.gRpcTask.BalanceGrpcTask
import co.sentinel.cosmos.task.gRpcTask.BondedValidatorsGrpcTask
import co.sentinel.cosmos.task.gRpcTask.DelegationsGrpcTask
import co.sentinel.cosmos.task.gRpcTask.NodeInfoGrpcTask
import co.sentinel.cosmos.task.gRpcTask.UnBondedValidatorsGrpcTask
import co.sentinel.cosmos.task.gRpcTask.UnBondingValidatorsGrpcTask
import co.sentinel.cosmos.task.gRpcTask.UnDelegationsGrpcTask
import co.sentinel.cosmos.task.gRpcTask.broadcast.BroadcastNodeSubscribeGrpcTask
import co.sentinel.cosmos.task.gRpcTask.broadcast.ConnectToNodeGrpcTask
import co.sentinel.cosmos.task.gRpcTask.broadcast.GenericGrpcTask
import co.sentinel.cosmos.utils.WKey
import co.sentinel.cosmos.utils.WUtil
import co.sentinel.dvpn.domain.core.DEFAULT_FEE
import co.sentinel.dvpn.domain.core.DEFAULT_FEE_AMOUNT
import co.sentinel.dvpn.domain.core.DEFAULT_GAS
import co.sentinel.dvpn.domain.core.denom
import co.sentinel.dvpn.domain.core.exception.AccountError
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.extension.toByteArray
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.hub.model.Session
import co.sentinel.dvpn.domain.features.wallet.model.Wallet
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository
import co.sentinel.dvpn.domain.features.wallet.tasks.PutWallet
import co.sentinel.dvpn.domain.features.wallet.tasks.results.GenerateKeywords
import com.google.protobuf2.Any
import cosmos.base.v1beta1.CoinOuterClass
import cosmos.distribution.v1beta1.Distribution
import cosmos.staking.v1beta1.Staking
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import tendermint.p2p.Types
import timber.log.Timber

class WalletRepositoryImpl(private val app: BaseCosmosApp) : WalletRepository {
    private val prefsStore = PrefsStore(app)

    // Account and wallet

    override suspend fun getAccount(): Either<Failure, Account> {
        return app.baseDao.onSelectAccount(app.baseDao.lastUser)?.let {
            Either.Right(it)
        } ?: Either.Left(AccountError)
    }

    override suspend fun restoreAccount(keywords: String): Either<Failure, Success> {
        return kotlin.runCatching {
            if (keywords.isBlank()) return@runCatching Either.Left(PutWallet.PutWalletFailure.EmptyKeywords)
            val keywordList = ArrayList<String>()
            keywordList.addAll(keywords.split(" "))

            if ((keywordList.size == 12 || keywordList.size == 24).not()
                || !areValidKeywords(keywords)
            ) {
                return Either.Left(PutWallet.PutWalletFailure.InvalidKeywords)
            }
            val entropy = WKey.toEntropy(keywordList)?.let { WUtil.ByteArrayToHexString(it) }
                ?: return Either.Left(PutWallet.PutWalletFailure.InvalidKeywords)

            val size = keywordList.size
            val chain = BaseChain.SENTINEL_MAIN
            val bip44 = false
            GenerateAccountTask(app, chain, bip44)
                .run("0", entropy, "$size").let {
                    if (it.isSuccess) {
                        fetchWalletStats()
                        Either.Right(Success)
                    } else {
                        Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                    }
                }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)
    }

    override suspend fun generateKeywords(): Either<Failure, GenerateKeywords.Success> {
        return kotlin.runCatching {
            val entropy = WKey.getEntropy()
            val entropyString = WUtil.ByteArrayToHexString(entropy)
            val keywordList = ArrayList<String>()
            keywordList.addAll(WKey.getRandomMnemonic(entropy))
            val chain = BaseChain.SENTINEL_MAIN
            val bip44 = false
            val dKey = WKey.getKeyWithPathfromEntropy(
                chain,
                entropyString,
                0,
                bip44
            )
            val address = WKey.getDpAddress(chain, dKey.publicKeyAsHex)

            Either.Right(GenerateKeywords.Success(keywordList, address, entropyString))
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)
    }

    override suspend fun generateAccount(): Either<Failure, Success> {
        return kotlin.runCatching {
            if (!app.baseDao.hasUser()) {
                val keywords: ArrayList<String> = ArrayList()
                val entropy = WKey.getEntropy()
                keywords.addAll(WKey.getRandomMnemonic(entropy))
                val size = keywords.size
                val chain = BaseChain.SENTINEL_MAIN
                val bip44 = false
                GenerateAccountTask(app, chain, bip44)
                    .run("0", WUtil.ByteArrayToHexString(entropy), "$size")
                    .let {
                        if (it.isSuccess) {
                            fetchWalletStats()
                            Either.Right(Success)
                        } else {
                            Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                        }
                    }
            } else {
                Either.Right(Success)
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)
    }

    override suspend fun generateAccount(
        entropy: String,
        keywords: List<String>
    ): Either<Failure, Success> {
        return kotlin.runCatching {
            GenerateSentinelAccountTask(app, entropy, keywords).run().let {
                if (it.isSuccess) {
                    fetchWalletStats()
                    Either.Right(Success)
                } else {
                    Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                }
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    }

    override suspend fun getWallet(): Either<Failure, Wallet> = kotlin.runCatching {
        val fetchBalanceResponse = fetchBalance()
        if (fetchBalanceResponse.isLeft) {
            return Either.Left(fetchBalanceResponse.requireLeft())
        }

        val accountBalances = fetchBalanceResponse.requireRight()
        val dvpnBalance =
            accountBalances.firstOrNull { balance -> balance.denom == denom }
                ?: Coin(DENOM_DVPN, "0")

        val account = getAccount()
        if (account.isLeft) return@runCatching Either.Left(account.requireLeft())
        val wallet = Wallet(
            address = account.requireRight().address,
            balance = dvpnBalance.amount.toInt(),
            currency = dvpnBalance.denom
        )

        Either.Right(wallet)
    }.onFailure { Timber.e(it) }
        .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun clearWallet() {
        app.baseDao.clearDB()
    }

    // fetch wallet stats

    override suspend fun fetchNodeInfo(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            NodeInfoGrpcTask(app, BaseChain.getChain(account.baseChain)).run().let {
                if (it.resultData is Types.DefaultNodeInfo) {
                    app.baseDao.mGRpcNodeInfo = it.resultData as Types.DefaultNodeInfo
                    Either.Right(Success)
                } else {
                    Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                }
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchAuthorization(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            AuthGrpcTask(app, BaseChain.getChain(account.baseChain), account.address).run().let {
                if (it.resultData is Any) {
                    app.baseDao.mGRpcAccount = it.resultData as Any
                    Either.Right(Success)
                } else {
                    Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                }
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchBondedValidators(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            BondedValidatorsGrpcTask(app, BaseChain.getChain(account.baseChain)).run()
                .let { result ->
                    if (result.resultData is ArrayList<*> && (result.resultData as ArrayList<*>).firstOrNull()
                            ?.let { it is Staking.Validator } == true
                    ) {
                        app.baseDao.mGRpcTopValidators.clear()
                        app.baseDao.mGRpcTopValidators.addAll(result.resultData as ArrayList<Staking.Validator>)
                        Either.Right(Success)
                    } else {
                        Either.Left(WalletTaskError(result.errorCode, result.errorMsg))
                    }
                }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchUnbondedValidators(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            UnBondedValidatorsGrpcTask(app, BaseChain.getChain(account.baseChain)).run()
                .let { result ->
                    if (result.resultData is ArrayList<*> && (result.resultData as ArrayList<*>).firstOrNull()
                            ?.let { it is Staking.Validator } == true
                    ) {
                        app.baseDao.mGRpcUnbondedValidators.clear()
                        app.baseDao.mGRpcUnbondedValidators.addAll(result.resultData as ArrayList<Staking.Validator>)
                        Either.Right(Success)
                    } else {
                        Either.Left(WalletTaskError(result.errorCode, result.errorMsg))
                    }
                }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchUnbondingValidators(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            UnBondingValidatorsGrpcTask(app, BaseChain.getChain(account.baseChain)).run()
                .let { result ->
                    if (result.resultData is ArrayList<*> && (result.resultData as ArrayList<*>).firstOrNull()
                            ?.let { it is Staking.Validator } == true
                    ) {
                        app.baseDao.mGRpcUnbondingValidators.clear()
                        app.baseDao.mGRpcUnbondingValidators.addAll(result.resultData as ArrayList<Staking.Validator>)
                        Either.Right(Success)
                    } else {
                        Either.Left(WalletTaskError(result.errorCode, result.errorMsg))
                    }
                }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchBalance(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            BalanceGrpcTask(app, BaseChain.getChain(account.baseChain), account.address).run().let {
                if (it.resultData is ArrayList<*>) {
                    val balance = it.resultData as ArrayList<CoinOuterClass.Coin>
                    app.baseDao.mGrpcBalance.clear()
                    if (balance.size > 0) {
                        for (coin in balance) {
                            app.baseDao.mGrpcBalance.add(Coin(coin.denom, coin.amount))
                        }
                    }

                    if (balance.none { coin -> coin.denom == DENOM_DVPN }) {
                        app.baseDao.mGrpcBalance.add(
                            Coin(
                                DENOM_DVPN,
                                "0"
                            )
                        )
                    }

                    Either.Right(Success)
                } else {
                    Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                }
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchDelegations(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            DelegationsGrpcTask(app, BaseChain.getChain(account.baseChain), account).run()
                .let { result ->
                    if (result.resultData is ArrayList<*> && (result.resultData as ArrayList<*>).firstOrNull()
                            ?.let { it is Staking.DelegationResponse } == true
                    ) {
                        app.baseDao.mGrpcDelegations.clear()
                        app.baseDao.mGrpcDelegations =
                            result.resultData as ArrayList<Staking.DelegationResponse>
                        Either.Right(Success)
                    } else {
                        Either.Left(WalletTaskError(result.errorCode, result.errorMsg))
                    }
                }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchUnboundingDelegations(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            UnDelegationsGrpcTask(app, BaseChain.getChain(account.baseChain), account).run()
                .let { result ->
                    if (result.resultData is ArrayList<*> && (result.resultData as ArrayList<*>).firstOrNull()
                            ?.let { it is Staking.UnbondingDelegation } == true
                    ) {
                        app.baseDao.mGrpcUndelegations.clear()
                        app.baseDao.mGrpcUndelegations =
                            result.resultData as ArrayList<Staking.UnbondingDelegation>
                        Either.Right(Success)
                    } else {
                        Either.Left(WalletTaskError(result.errorCode, result.errorMsg))
                    }
                }

        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchRewards(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            AllRewardGrpcTask(app, BaseChain.getChain(account.baseChain), account).run()
                .let { result ->
                    if (result.resultData is ArrayList<*> && (result.resultData as ArrayList<*>).firstOrNull()
                            ?.let { it is Distribution.DelegationDelegatorReward } == true
                    ) {
                        app.baseDao.mGrpcRewards.clear()
                        app.baseDao.mGrpcRewards =
                            result.resultData as ArrayList<Distribution.DelegationDelegatorReward>
                        Either.Right(Success)
                    } else {
                        Either.Left(WalletTaskError(result.errorCode, result.errorMsg))
                    }
                }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun fetchWalletStats(): Either<Failure, Success> {
        val account = getAccount()
        if (account.isLeft) return Either.Left(account.requireLeft())
        return fetchAll(account.requireRight())
    }

    override suspend fun fetchBalance(): Either<Failure, ArrayList<Coin>> {
        val account = getAccount()
        if (account.isLeft) return Either.Left(account.requireLeft())
        return fetchBalance(account.requireRight()).let {
            if (it.isRight) {
                Either.Right(app.baseDao.mGrpcBalance)
            } else {
                Either.Left(it.requireLeft())
            }
        }
    }

    private suspend fun fetchAll(account: Account): Either<Failure, Success> =
        kotlin.runCatching {
            withContext(Dispatchers.Default) {
                listOf(
                    async(start = CoroutineStart.LAZY) { fetchNodeInfo(account) },
                    async(start = CoroutineStart.LAZY) { fetchAuthorization(account) },
                    async(start = CoroutineStart.LAZY) { fetchBondedValidators(account) },
                    async(start = CoroutineStart.LAZY) { fetchUnbondedValidators(account) },
                    async(start = CoroutineStart.LAZY) { fetchUnbondingValidators(account) },
                    async(start = CoroutineStart.LAZY) { fetchBalance(account) },
                    async(start = CoroutineStart.LAZY) { fetchDelegations(account) },
                    async(start = CoroutineStart.LAZY) { fetchUnboundingDelegations(account) },
                    async(start = CoroutineStart.LAZY) { fetchRewards(account) }
                ).awaitAll().let { results ->
                    if (results.any { it.isLeft }) {
                        Either.Left(results.first { it.isLeft }.requireLeft())
                    } else {
                        Either.Right(Success)
                    }
                }
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)

    // Session

    override suspend fun startNodeSession(messages: List<Any>): Either<Failure, Success> {
        return kotlin.runCatching {
            val accountResult = getAccount()
            when {
                accountResult.isRight -> {
                    val account = accountResult.requireRight()
                    fetchAll(account)
                    ConnectToNodeGrpcTask(
                        app,
                        BaseChain.getChain(account.baseChain),
                        account,
                        messages,
                        DEFAULT_FEE,
                        app.baseDao.chainIdGrpc
                    ).run(prefsStore.retrievePasscode()) // password confirmation
                        .let {
                            if (!it.isSuccess) {
                                Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                            } else {
                                Either.Right(Success)
                            }
                        }
                }
                else -> Either.Left(accountResult.requireLeft())
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)
    }

    override suspend fun getSignature(session: Session): Either<Failure, String> =
        kotlin.runCatching {
            val accountResult = getAccount()
            when {
                accountResult.isRight -> {
                    val account = app.baseDao.onSelectAccount(app.baseDao.lastUser)
                    val entropy = CryptoHelper.doDecryptData(
                        app.context.getString(R.string.key_mnemonic) + account.uuid,
                        account.resource,
                        account.spec
                    )
                    val deterministicKey = WKey.getKeyWithPathfromEntropy(
                        BaseChain.getChain(account.baseChain),
                        entropy,
                        account.path.toInt(),
                        account.newBip44
                    )
                    val signature = Signer.getGrpcByteSingleSignature(
                        deterministicKey,
                        session.id.toByteArray()
                    ).let {
                        Base64.encodeToString(it, Base64.DEFAULT)
                    }
                    Either.Right(signature)
                }
                else -> Either.Left(accountResult.requireLeft())
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)


    // Sign and broadcast

    override suspend fun signSubscribedRequestAndBroadcast(
        nodeAddress: String,
        subscribeMessage: Any
    ): Either<Failure, Success> {
        return kotlin.runCatching {
            val accountResult = getAccount()
            when {
                accountResult.isRight -> {
                    val account = accountResult.requireRight()
                    fetchAll(account)
                    BroadcastNodeSubscribeGrpcTask(
                        app,
                        BaseChain.getChain(account.baseChain),
                        account,
                        nodeAddress,
                        subscribeMessage,
                        DEFAULT_FEE,
                        app.baseDao.chainIdGrpc
                    ).run(prefsStore.retrievePasscode()) // password confirmation
                        .let {
                            if (!it.isSuccess) {
                                Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                            } else {
                                Either.Right(Success)
                            }
                        }
                }
                else -> Either.Left(accountResult.requireLeft())
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)
    }

    override suspend fun signRequestAndBroadcast(
        gasFactor: Int,
        messages: List<Any>
    ): Either<Failure, Success> {
        return kotlin.runCatching {
            val accountResult = getAccount()
            when {
                accountResult.isRight -> {
                    val account = accountResult.requireRight()
                    fetchAll(account)

                    val gas = DEFAULT_GAS + (DEFAULT_GAS / 10 * gasFactor)
                    val feePrice = DEFAULT_FEE_AMOUNT + (DEFAULT_FEE_AMOUNT / 10 * gasFactor)

                    val fee = Fee(gas.toString(), arrayListOf(Coin(denom, feePrice.toString())))

                    GenericGrpcTask(
                        app,
                        BaseChain.getChain(account.baseChain),
                        account,
                        messages,
                        fee,
                        app.baseDao.chainIdGrpc
                    ).run(prefsStore.retrievePasscode()) // password confirmation
                        .let {
                            if (!it.isSuccess) {
                                Either.Left(WalletTaskError(it.errorCode, it.errorMsg))
                            } else {
                                Either.Right(Success)
                            }
                        }
                }
                else -> Either.Left(accountResult.requireLeft())
            }
        }.onFailure { Timber.e(it) }
            .getOrNull() ?: Either.Left(Failure.AppError)
    }

}