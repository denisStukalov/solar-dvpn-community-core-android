package ee.solarlabs.community.core.controllers

import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.solar.model.OrderBy
import co.sentinel.dvpn.domain.features.solar.model.Status
import co.sentinel.dvpn.domain.features.solar.model.request.GetCountriesByContinentRequest
import co.sentinel.dvpn.domain.features.solar.model.request.GetNodesByAddressRequest
import co.sentinel.dvpn.domain.features.solar.model.request.GetNodesRequest
import co.sentinel.dvpn.domain.features.solar.tasks.GetContinents
import co.sentinel.dvpn.domain.features.solar.tasks.GetCountries
import co.sentinel.dvpn.domain.features.solar.tasks.GetCountriesByContinent
import co.sentinel.dvpn.domain.features.solar.tasks.GetNodes
import co.sentinel.dvpn.domain.features.solar.tasks.GetNodesByAddresses
import ee.solarlabs.community.core.extension.anyToNull
import ee.solarlabs.community.core.mapper.nodes.GetNodesByAddressesResponseMapper
import ee.solarlabs.community.core.mapper.nodes.GetNodesResponseMapper
import ee.solarlabs.community.core.model.HttpError
import ee.solarlabs.community.core.model.HttpError.Companion.internalServer
import ee.solarlabs.community.core.model.nodes.request.GetNodesByAddressesRequest
import ee.solarlabs.community.core.model.nodes.response.GetContinentsResponse
import ee.solarlabs.community.core.model.nodes.response.GetCountriesResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.java.KoinJavaComponent

fun Application.routeNodes() {
    val getContinents: GetContinents by KoinJavaComponent.inject(GetContinents::class.java)
    val getCountries: GetCountries by KoinJavaComponent.inject(GetCountries::class.java)
    val getCountriesByContinent: GetCountriesByContinent by KoinJavaComponent.inject(
        GetCountriesByContinent::class.java
    )
    val getNodes: GetNodes by KoinJavaComponent.inject(GetNodes::class.java)
    val getNodesByAddresses: GetNodesByAddresses by KoinJavaComponent.inject(GetNodesByAddresses::class.java)

    routing {
        /**
         * This method is used to retrieve list of supported continents with their number of nodes.
         */
        get("/api/continents") {
            getContinents().let {
                if (it.isRight) {
                    val response = it.requireRight().countries.map { continent ->
                        GetContinentsResponse(
                            code = continent.code,
                            nodesCount = continent.nodesCount
                        )
                    }
                    return@get call.respond(HttpStatusCode.OK, response)
                } else {
                    return@get call.respond(HttpStatusCode.InternalServerError, internalServer)
                }
            }
        }

        /**
         * This method is used to retrieve list of supported countries.
         */
        get("/api/countries") {
            getCountries().let {
                if (it.isRight) {
                    val response = it.requireRight().countries.map { country ->
                        GetCountriesResponse(
                            code = country.code,
                            nodesCount = country.nodesCount
                        )
                    }
                    return@get call.respond(HttpStatusCode.OK, response)
                } else {
                    return@get call.respond(HttpStatusCode.InternalServerError, internalServer)
                }
            }
        }

        /**
         * This method is used to retrieve list of supported countries of selected continent.
         */
        get("/api/countriesByContinent") {
            val continent = call.request.queryParameters["continent"] ?: let {
                return@get call.respond(HttpStatusCode.BadRequest, HttpError.badRequest)
            }

            getCountriesByContinent(GetCountriesByContinentRequest(continent)).let {
                if (it.isRight) {
                    val response = it.requireRight().countries.map { country ->
                        GetCountriesResponse(
                            code = country.code,
                            nodesCount = country.nodesCount
                        )
                    }
                    return@get call.respond(HttpStatusCode.OK, response)
                } else {
                    return@get call.respond(HttpStatusCode.InternalServerError, internalServer)
                }
            }
        }

        /**
         * This method is used to retrieve list of DVPN nodes.
         */
        get("/api/nodes") {
            val status = call.request.queryParameters["status"]?.runCatching {
                enumValueOf<Status>(uppercase())
            }?.getOrNull()
            val country = call.request.queryParameters["country"]?.anyToNull()
            val continent = call.request.queryParameters["continent"]?.anyToNull()
            val query = call.request.queryParameters["q"]?.anyToNull()
            val orderBy = call.request.queryParameters["orderBy"]?.runCatching {
                enumValueOf<OrderBy>(uppercase())
            }?.getOrNull()
            val page = call.request.queryParameters["page"]?.let {
                it.runCatching { it.toInt() }.getOrNull()
            }

            getNodes(
                GetNodesRequest(
                    continent = continent,
                    country = country,
                    status = status,
                    orderBy = orderBy,
                    query = query,
                    page = page
                )
            ).let {
                if (it.isRight) {
                    return@get call.respond(
                        HttpStatusCode.OK, GetNodesResponseMapper.map(it.requireRight())
                    )
                } else {
                    return@get call.respond(HttpStatusCode.InternalServerError, internalServer)
                }
            }
        }

        /**
         * This method is used to retrieve list of DVPN nodes by list of their blockchain addresses.
         */
        post("/api/nodesByAddress") {
            val request =
                kotlin.runCatching { call.receive<GetNodesByAddressesRequest>() }.getOrNull()
                    ?: let {
                        return@post call.respond(HttpStatusCode.BadRequest, HttpError.badRequest)
                    }

            getNodesByAddresses(
                GetNodesByAddressRequest(
                    request.blockchainAddresses,
                    request.page
                )
            ).let {
                if (it.isRight) {
                    return@post call.respond(
                        HttpStatusCode.OK, GetNodesByAddressesResponseMapper.map(it.requireRight())
                    )
                } else {
                    return@post call.respond(HttpStatusCode.InternalServerError, internalServer)
                }
            }
        }
    }
}
