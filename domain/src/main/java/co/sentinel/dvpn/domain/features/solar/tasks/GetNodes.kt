package co.sentinel.dvpn.domain.features.solar.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.solar.model.Node
import co.sentinel.dvpn.domain.features.solar.model.request.GetNodesRequest
import co.sentinel.dvpn.domain.features.solar.source.SolarRepository

class GetNodes(
    private val solarRepository: SolarRepository
) {
    suspend operator fun invoke(params: GetNodesRequest): Either<Failure, Success> =
        solarRepository.fetchNodes(params).let {
            when {
                it.isRight -> Either.Right(
                    Success(
                        it.requireRight().currentPage,
                        it.requireRight().data,
                        it.requireRight().total
                    )
                )
                else -> Either.Left(it.requireLeft())
            }
        }

    data class Success(
        val currentPage: Int,
        val data: List<Node>,
        val total: Int
    )
}