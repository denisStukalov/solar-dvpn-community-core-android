package co.sentinel.dvpn.domain.features.hub.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.hub.source.HubRemoteRepository
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class DeleteSubscription(
    private val hubRemoteRepository: HubRemoteRepository,
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(params: Params): Either<Failure, Success> {
        val fetchSubscriptionsResult = hubRemoteRepository.fetchSubscriptions()
        if (fetchSubscriptionsResult.isLeft) {
            return Either.Left(fetchSubscriptionsResult.requireLeft())
        }

        val subscription = fetchSubscriptionsResult.requireRight().lastOrNull {
            it.node == params.nodeAddress
        } ?: return Either.Left(DeleteSubscriptionFailure.NodeNotFound)

        val subscriptions = listOf(subscription.id)
        val generateCancelSubscriptionMessage =
            hubRemoteRepository.generateCancelNodeSubscriptionMessage(subscriptions)

        return walletRepository.signRequestAndBroadcast(
            subscriptions.size,
            generateCancelSubscriptionMessage
        )
    }

    data class Params(val nodeAddress: String)

    sealed class DeleteSubscriptionFailure : Failure.FeatureFailure() {
        object NodeNotFound : DeleteSubscriptionFailure()
    }
}