package co.sentinel.dvpn.hub.tasks

import co.sentinel.cosmos.dao.Account
import com.google.protobuf2.Any
import sentinel.session.v1.Msg

object GenerateStartActiveSessionMessage {
    fun execute(account: Account, subscriptionId: Long, nodeAddress: String) =
        Any.newBuilder()
            .setTypeUrl("/sentinel.session.v1.MsgStartRequest")
            .setValue(
                Msg.MsgStartRequest.newBuilder()
                    .setId(subscriptionId)
                    .setFrom(account.address)
                    .setNode(nodeAddress)
                    .build()
                    .toByteString()
            )
            .build()

}