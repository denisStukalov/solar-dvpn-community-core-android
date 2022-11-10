package co.sentinel.dvpn.domain.features.dvpn.tasks.connection

import co.sentinel.dvpn.domain.core.DVPN_NODE_NAME
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.dvpn.SetTunnelState
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository
import co.sentinel.dvpn.domain.features.hub.source.HubRemoteRepository
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class DeleteSessions(
    private val hubRemoteRepository: HubRemoteRepository,
    private val walletRepository: WalletRepository,
    private val dvpnRepository: DVPNRepository
) {

    suspend operator fun invoke(): Either<Failure, Success> {
        val generateStopActiveSessionsMessage =
            hubRemoteRepository.generateStopActiveSessionsMessage()
        if (generateStopActiveSessionsMessage.isLeft) {
            return Either.Left(generateStopActiveSessionsMessage.requireLeft())
        }

        if (generateStopActiveSessionsMessage.requireRight().isEmpty())
            return Either.Right(Success)

        val broadcastResult = walletRepository.signRequestAndBroadcast(
            messages = generateStopActiveSessionsMessage.requireRight()
        )
        if (broadcastResult.isLeft) {
            return Either.Left(broadcastResult.requireLeft())
        }

        dvpnRepository.setTunnelState(
            SetTunnelState.SetTunnelStateParams(
                DVPN_NODE_NAME,
                DvpnTunnel.State.DOWN
            )
        )
        return Either.Right(Success)
    }
}