package co.sentinel.dvpn.domain.features.dvpn.tasks.connection

import co.sentinel.dvpn.domain.core.DVPN_NODE_NAME
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.dvpn.SetTunnelState
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class DeleteConnection(
    private val dvpnRepository: DVPNRepository
) {

    suspend operator fun invoke(): Either<Failure, Success> {
        val setTunnelStateResult = dvpnRepository.setTunnelState(
            SetTunnelState.SetTunnelStateParams(
                DVPN_NODE_NAME, DvpnTunnel.State.DOWN
            )
        )
        if (setTunnelStateResult.isLeft) {
            return Either.Left(setTunnelStateResult.requireLeft())
        }

        return Either.Right(Success)
    }
}