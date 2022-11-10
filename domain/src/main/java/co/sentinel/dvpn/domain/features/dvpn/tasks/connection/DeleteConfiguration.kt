package co.sentinel.dvpn.domain.features.dvpn.tasks.connection

import co.sentinel.dvpn.domain.core.DVPN_NODE_NAME
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.dvpn.DeleteTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class DeleteConfiguration(
    private val dvpnRepository: DVPNRepository
) {

    suspend operator fun invoke(): Either<Failure, Success> {
        val deleteTunnelResult =
            dvpnRepository.delete(DeleteTunnel.DeleteTunnelParams(DVPN_NODE_NAME))
        if (deleteTunnelResult.isLeft) {
            return Either.Left(deleteTunnelResult.requireLeft())
        }

        return Either.Right(Success)
    }
}