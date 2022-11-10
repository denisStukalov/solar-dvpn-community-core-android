package co.sentinel.dvpn.domain.features.dvpn.tasks.connection

import co.sentinel.dvpn.domain.core.DVPN_NODE_NAME
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.dvpn.GetTunnel
import co.sentinel.dvpn.domain.features.dvpn.model.DvpnTunnel
import co.sentinel.dvpn.domain.features.dvpn.source.DVPNRepository

class GetConnection(
    private val dvpnRepository: DVPNRepository
) {

    suspend operator fun invoke(): Either<Failure, Success> =
        when (val getTunnelResult = dvpnRepository.getTunnel(DVPN_NODE_NAME)) {
            is Either.Left -> {
                if (getTunnelResult.requireLeft() == GetTunnel.GetTunnelFailure.TunnelNotFound) {
                    Either.Right(
                        Success(
                            nodeAddress = "",
                            isConnected = false
                        )
                    )
                } else {
                    Either.Left(getTunnelResult.requireLeft())
                }
            }
            is Either.Right -> {
                Either.Right(
                    Success(
                        nodeAddress = getTunnelResult.requireRight().tunnel.nodeAddress ?: "",
                        isConnected = getTunnelResult.requireRight().tunnel.state == DvpnTunnel.State.UP
                    )
                )
            }
        }

    data class Success(val nodeAddress: String, val isConnected: Boolean)
}