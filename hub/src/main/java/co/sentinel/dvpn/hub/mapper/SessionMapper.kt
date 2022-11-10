package co.sentinel.dvpn.hub.mapper

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.hub.model.Session
import sentinel.session.v1.SessionOuterClass

object SessionMapper : Mapper<SessionOuterClass.Session, Session> {
    override fun map(obj: SessionOuterClass.Session) = Session(
        id = obj.id,
        node = obj.node
    )
}