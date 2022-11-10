package co.sentinel.dvpn.domain.features.solar.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.solar.model.Country
import co.sentinel.dvpn.domain.features.solar.source.SolarRepository

class GetCountries(private val solarRepository: SolarRepository) {

    suspend operator fun invoke(): Either<Failure, Success> =
        solarRepository.fetchCountries().let {
            when {
                it.isRight -> Either.Right(Success(it.requireRight().countries))
                else -> Either.Left(it.requireLeft())
            }
        }

    data class Success(val countries: List<Country>)
}