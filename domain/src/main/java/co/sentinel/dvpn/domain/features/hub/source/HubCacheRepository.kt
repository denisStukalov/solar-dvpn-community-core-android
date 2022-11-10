package co.sentinel.dvpn.domain.features.hub.source

import co.sentinel.dvpn.domain.features.hub.model.Session

interface HubCacheRepository {
    suspend fun setLastSession(session: Session)
    suspend fun getLastSession(): Session?
}