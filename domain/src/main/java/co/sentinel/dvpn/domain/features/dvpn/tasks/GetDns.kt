package co.sentinel.dvpn.domain.features.dvpn.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.features.dvpn.model.DnsServer
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetDns(
    private val dvpnRepository: DVPNRepository
){

    suspend operator fun invoke(): Either<Failure, DnsServer> =
        Either.Right(dvpnRepository.getDefaultDns())
}