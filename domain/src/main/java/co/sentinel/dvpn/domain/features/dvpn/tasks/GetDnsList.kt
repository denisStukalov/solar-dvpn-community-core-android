package co.sentinel.dvpn.domain.features.dvpn.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.features.dvpn.model.DnsServer
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetDnsList(
    private val dvpnRepository: DVPNRepository
) {
    suspend operator fun invoke(): Either<Failure, List<DnsServer>> =
        Either.Right(dvpnRepository.getDnsList())
}