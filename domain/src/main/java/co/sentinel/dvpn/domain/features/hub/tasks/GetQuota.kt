package co.sentinel.dvpn.domain.features.hub.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.hub.model.Quota
import co.sentinel.dvpn.domain.features.hub.source.HubRemoteRepository

class GetQuota(private val hubRemoteRepository: HubRemoteRepository) {

    suspend operator fun invoke(params: Params): Either<Failure, Quota> {
        val fetchSubscriptionsResult = hubRemoteRepository.fetchSubscriptions()
        if (fetchSubscriptionsResult.isLeft) {
            return Either.Left(fetchSubscriptionsResult.requireLeft())
        }

        val subscription = fetchSubscriptionsResult.requireRight().lastOrNull {
            it.node == params.nodeAddress
        } ?: return Either.Left(GetQuotaFailure.SubscriptionNotFound)

        val fetchQuotaResult = hubRemoteRepository.fetchQuota(subscription.id)
        if (fetchQuotaResult.isLeft) {
            return Either.Left(fetchQuotaResult.requireLeft())
        }

        return Either.Right(fetchQuotaResult.requireRight())
    }

    data class Params(val nodeAddress: String)

    sealed class GetQuotaFailure : Failure.FeatureFailure() {
        object SubscriptionNotFound : GetQuotaFailure()
    }
}