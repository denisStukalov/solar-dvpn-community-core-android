package co.sentinel.dvpn.domain.features.solar.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.solar.model.Continent
import co.sentinel.dvpn.domain.features.solar.source.SolarRepository

class GetContinents(private val solarRepository: SolarRepository) {

    suspend operator fun invoke(): Either<Failure, Success> =
        solarRepository.fetchContinents().let {
            when {
                it.isRight -> Either.Right(Success(it.requireRight().continents))
                else -> Either.Left(it.requireLeft())
            }
        }

    data class Success(val countries: List<Continent>)
}