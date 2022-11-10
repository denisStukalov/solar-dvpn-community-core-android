package co.sentinel.dvpn.domain.features.dvpn.tasks.connection

import co.sentinel.dvpn.domain.features.dvpn.model.ConnectionEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ConnectionEventBus {
    private val _events = MutableSharedFlow<ConnectionEvent>()
    val events = _events.asSharedFlow()

    suspend fun emitEvent(event: ConnectionEvent) {
        _events.emit(event)
    }
}