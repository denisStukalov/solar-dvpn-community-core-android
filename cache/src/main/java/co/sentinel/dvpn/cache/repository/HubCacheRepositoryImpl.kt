package co.sentinel.dvpn.cache.repository

import co.sentinel.dvpn.cache.data.LastSessionDatastore
import co.sentinel.dvpn.domain.features.hub.model.Session
import co.sentinel.dvpn.domain.features.hub.source.HubCacheRepository

class HubCacheRepositoryImpl(
    private val lastSessionDatastore: LastSessionDatastore
) : HubCacheRepository {

    override suspend fun setLastSession(session: Session) {
        lastSessionDatastore.setLastSession(session)
    }

    override suspend fun getLastSession(): Session? {
        return lastSessionDatastore.getLastSession()
    }
}