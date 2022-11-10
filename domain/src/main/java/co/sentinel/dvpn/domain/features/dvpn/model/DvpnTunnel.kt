package co.sentinel.dvpn.domain.features.dvpn.model

data class DvpnTunnel(
    val name: String,
    var state: State,
    var config: TunnelConfig? = null,
    var nodeAddress: String? = null,
    var subscriptionId: Long? = null,
    var duration: Long? = null
    // var statistics: Statistics? = null
) {
    enum class State {
        DOWN, TOGGLE, UP;

        /**
         * Get the state of a [DvpnTunnel]
         *
         * @param running boolean indicating if the tunnel is running.
         * @return State of the tunnel based on whether or not it is running.
         */
        fun of(running: Boolean): State {
            return if (running) UP else DOWN
        }

        companion object {
            fun of(running: Boolean): State {
                return if (running) UP else DOWN
            }
        }
    }
}