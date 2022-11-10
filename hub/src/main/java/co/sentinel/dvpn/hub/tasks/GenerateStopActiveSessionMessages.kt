package co.sentinel.dvpn.hub.tasks

import co.sentinel.cosmos.dao.Account
import com.google.protobuf2.Any
import sentinel.session.v1.Msg
import sentinel.session.v1.SessionOuterClass

object GenerateStopActiveSessionMessages {
    fun execute(account: Account, sessions: List<SessionOuterClass.Session>) =
        sessions.map { session ->
            Any.newBuilder()
                .setTypeUrl("/sentinel.session.v1.MsgEndRequest")
                .setValue(
                    Msg.MsgEndRequest.newBuilder()
                        .setId(session.id)
                        .setFrom(account.address)
                        .build().toByteString()
                )
                .build()
        }
}