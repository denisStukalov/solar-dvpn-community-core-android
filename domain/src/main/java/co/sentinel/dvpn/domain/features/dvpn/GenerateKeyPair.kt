package co.sentinel.dvpn.domain.features.dvpn

import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GenerateKeyPair(
    private val repository: DVPNRepository
) {

    operator fun invoke() = Either.Right(repository.generateKeyPair())

}