package ee.solarlabs.community.core.plugins

import ee.solarlabs.community.core.controllers.routeConnection
import ee.solarlabs.community.core.controllers.routeDns
import ee.solarlabs.community.core.controllers.routeNodes
import ee.solarlabs.community.core.controllers.routePurchases
import ee.solarlabs.community.core.controllers.routeRegistry
import ee.solarlabs.community.core.controllers.routeSubscriptions
import ee.solarlabs.community.core.controllers.routeWallet
import io.ktor.server.application.Application

fun Application.configureRouting() {
    routeConnection()
    routeDns()
    routeNodes()
    routePurchases()
    routeRegistry()
    routeSubscriptions()
    routeWallet()
}
