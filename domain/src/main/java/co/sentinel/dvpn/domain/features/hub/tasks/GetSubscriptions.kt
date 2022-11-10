package co.sentinel.dvpn.domain.features.hub.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.hub.source.HubRemoteRepository

class GetSubscriptions(private val hubRemoteRepository: HubRemoteRepository) {

    suspend operator fun invoke(): Either<Failure, List<String>> {
        val fetchSubscriptionsResult = hubRemoteRepository.fetchSubscriptions()
        if (fetchSubscriptionsResult.isLeft) {
            return Either.Left(fetchSubscriptionsResult.requireLeft())
        }

        return Either.Right(fetchSubscriptionsResult.requireRight().map { it.node })
    }
}