package co.sentinel.dvpn.domain.features.dvpn.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class PutDns(
    private val dvpnRepository: DVPNRepository
) {

    data class Params(val dnsName: String)

    suspend operator fun invoke(params: Params): Either<Failure, Success> {
        val selectedDns = dvpnRepository.getDnsList().firstOrNull {
            it.name.name.equals(params.dnsName, ignoreCase = true)
        } ?: return Either.Left(PutDnsFailure.UnknownDnsServer)

        return dvpnRepository.updateDns(selectedDns)
    }

    sealed class PutDnsFailure : Failure.FeatureFailure() {
        object UnknownDnsServer : PutDnsFailure()
    }
}