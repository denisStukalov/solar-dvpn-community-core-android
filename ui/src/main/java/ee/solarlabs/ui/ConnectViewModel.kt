package ee.solarlabs.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.dvpn.GetVpnServiceIntent
import co.sentinel.dvpn.domain.features.dvpn.model.ConnectionEvent
import co.sentinel.dvpn.domain.features.dvpn.tasks.connection.ConnectionEventBus
import co.sentinel.dvpn.domain.features.dvpn.tasks.connection.PostConnection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConnectViewModel : ViewModel(), KoinComponent {
    private val getVpnServiceIntent: GetVpnServiceIntent by inject()
    private val postConnection: PostConnection by inject()
    private val connectionEventBus by inject<ConnectionEventBus>()

    private val _connectionState: MutableLiveData<ConnectionState> = MutableLiveData()
    val connectionState: LiveData<ConnectionState> = _connectionState

    var nodeAddress: String? = null

    init {
        viewModelScope.launch {
            connectionEventBus.events.filter { it is ConnectionEvent.AttemptToConnect }
                .collectLatest {
                    it as ConnectionEvent.AttemptToConnect
                    nodeAddress = it.nodeAddress
                    _connectionState.value = ConnectionState.CheckPermission
                }
        }
    }

    /**
     * When receiving the connect event from the core, check if vpn permission is granted by launching this intent.
     */
    fun vpnCheckIntent(context: Context) =
        getVpnServiceIntent(context).let { if (it.isRight) it.requireRight() else null }

    fun attemptConnection() {
        val param = nodeAddress ?: return
        _connectionState.value = ConnectionState.AttemptingConnection
        postConnection(viewModelScope, PostConnection.Params(nodeAddress = param)) {
            _connectionState.value = ConnectionState.Done
            if (it.isLeft) {
                viewModelScope.launch {
                    connectionEventBus.emitEvent(ConnectionEvent.ConnectionError(it.requireLeft()))
                }
            }
        }
    }

}