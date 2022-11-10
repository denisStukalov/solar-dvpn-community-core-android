package ee.solarlabs.community.core.plugins

import io.ktor.serialization.gson.GsonWebsocketContentConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        contentConverter = GsonWebsocketContentConverter()
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}
