package co.sentinel.dvpn.domain.features.hub.tasks

import co.sentinel.dvpn.domain.core.DEFAULT_FEE_AMOUNT
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.core.generateCoinFromPrice
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.hub.source.HubRemoteRepository
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class PostSubscription(
    private val hubRemoteRepository: HubRemoteRepository,
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(params: Params): Either<Failure, Success> {
        val fetchBalanceResponse = walletRepository.fetchBalance()
        if (fetchBalanceResponse.isLeft) {
            return Either.Left(fetchBalanceResponse.requireLeft())
        }

        val coin = generateCoinFromPrice("${params.amount}${params.currency}")
        val accountBalances = fetchBalanceResponse.requireRight()
        accountBalances.firstOrNull { balance -> balance.denom == params.currency }?.let {
            if (it.amount.toLong() < (coin.amount.toLong() + DEFAULT_FEE_AMOUNT)) {
                return Either.Left(PostSubscriptionFailure.PaymentRequiredFailure)
            }
        } ?: return Either.Left(PostSubscriptionFailure.PaymentRequiredFailure)

        val generateCreateNodeSubscriptionMessage =
            hubRemoteRepository.generateCreateNodeSubscriptionMessage(
                params.nodeAddress,
                "${params.amount}${params.currency}"
            )

        return walletRepository.signSubscribedRequestAndBroadcast(
            nodeAddress = params.nodeAddress,
            subscribeMessage = generateCreateNodeSubscriptionMessage
        )
    }

    data class Params(val nodeAddress: String, val amount: String, val currency: String)

    sealed class PostSubscriptionFailure : Failure.FeatureFailure() {
        object PaymentRequiredFailure : PostSubscriptionFailure()
    }
}