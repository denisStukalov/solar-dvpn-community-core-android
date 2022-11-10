package co.sentinel.dvpn.domain.features.wallet.source

import co.sentinel.cosmos.dao.Account
import co.sentinel.cosmos.model.type.Coin
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.hub.model.Session
import co.sentinel.dvpn.domain.features.wallet.model.Wallet
import co.sentinel.dvpn.domain.features.wallet.tasks.results.GenerateKeywords
import com.google.protobuf2.Any

interface WalletRepository {

    // Account and wallet
    suspend fun getAccount(): Either<Failure, Account>
    suspend fun restoreAccount(keywords: String): Either<Failure, Success>
    suspend fun generateKeywords(): Either<Failure, GenerateKeywords.Success>
    suspend fun generateAccount(): Either<Failure, Success>
    suspend fun generateAccount(
        entropy: String,
        keywords: List<String>
    ): Either<Failure, Success>

    /**
     * Get wallet information. See [co.sentinel.dvpn.domain.features.wallet.model.Wallet].
     */
    suspend fun getWallet(): Either<Failure, Wallet>

    /**
     * Clear wallet information.
     */
    suspend fun clearWallet()

    // fetch wallet stats
    suspend fun fetchNodeInfo(account: Account): Either<Failure, Success>
    suspend fun fetchAuthorization(account: Account): Either<Failure, Success>
    suspend fun fetchBondedValidators(account: Account): Either<Failure, Success>
    suspend fun fetchUnbondedValidators(account: Account): Either<Failure, Success>
    suspend fun fetchUnbondingValidators(account: Account): Either<Failure, Success>
    suspend fun fetchBalance(account: Account): Either<Failure, Success>
    suspend fun fetchDelegations(account: Account): Either<Failure, Success>
    suspend fun fetchUnboundingDelegations(account: Account): Either<Failure, Success>
    suspend fun fetchRewards(account: Account): Either<Failure, Success>
    suspend fun fetchWalletStats(): Either<Failure, Success>
    suspend fun fetchBalance(): Either<Failure, ArrayList<Coin>>

    // Session
    suspend fun startNodeSession(messages: List<Any>): Either<Failure, Success>
    suspend fun getSignature(session: Session): Either<Failure, String>

    // Sign and broadcast
    suspend fun signSubscribedRequestAndBroadcast(
        nodeAddress: String,
        subscribeMessage: Any
    ): Either<Failure, Success>

    suspend fun signRequestAndBroadcast(
        gasFactor: Int = 0,
        messages: List<Any>
    ): Either<Failure, Success>
}