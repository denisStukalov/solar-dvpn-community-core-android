package co.sentinel.dvpn.core.model

import co.sentinel.dvpn.domain.core.functional.Keyed
import com.wireguard.android.backend.Statistics
import com.wireguard.android.backend.Tunnel
import com.wireguard.config.Config

class TunnelWrapper(
    private var name: String,
    var state: Tunnel.State,
    var config: Config? = null,
    var statistics: Statistics? = null,
    var duration: Long? = null
) : Keyed<String>, Tunnel {
    override val key: String
        get() = name

    override fun getName() = name

    override fun onStateChange(state: Tunnel.State) {
        onStateChanged(state)
    }

    fun onStateChanged(state: Tunnel.State): Tunnel.State {
        if (state != Tunnel.State.UP) {
            onStatisticsChanged(null)
            onDurationChanged(null)
        }
        this.state = state
        return state
    }

    fun onStatisticsChanged(statistics: Statistics?): Statistics? {
        this.statistics = statistics
        return statistics
    }

    fun onDurationChanged(duration: Long?) {
        this.duration = duration
    }

    fun onNameChanged(name: String) {
        this.name = name
    }

}