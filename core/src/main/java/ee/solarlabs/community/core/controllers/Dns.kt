package ee.solarlabs.community.core.controllers

import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.dvpn.tasks.GetDns
import co.sentinel.dvpn.domain.features.dvpn.tasks.GetDnsList
import co.sentinel.dvpn.domain.features.dvpn.tasks.PutDns
import ee.solarlabs.community.core.mapper.dns.DnsListResponseMapper
import ee.solarlabs.community.core.mapper.dns.DnsResponseMapper
import ee.solarlabs.community.core.model.ErrorWrapper
import ee.solarlabs.community.core.model.HttpError.Companion.badRequest
import ee.solarlabs.community.core.model.HttpError.Companion.internalServer
import ee.solarlabs.community.core.model.dns.request.DnsResponse
import ee.solarlabs.community.core.model.dns.response.DnsRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import org.koin.java.KoinJavaComponent.inject

fun Application.routeDns() {
    val getDns: GetDns by inject(GetDns::class.java)
    val getDnsList: GetDnsList by inject(GetDnsList::class.java)
    val putDns: PutDns by inject(PutDns::class.java)

    routing {
        /**
         * Return the currently selected dns server. Defaults to Handshake.
         */
        get("/api/dns/current") {
            val response: DnsResponse = getDns().let {
                if (it.isRight) {
                    DnsResponseMapper.map(it.requireRight())
                } else {
                    null
                }
            } ?: return@get call.respond(HttpStatusCode.InternalServerError, internalServer)

            call.respond(HttpStatusCode.OK, response)
        }

        /**
         * Return the list of available dns servers.
         */
        get("/api/dns/list") {
            val response = getDnsList().let {
                if (it.isRight) {
                    DnsListResponseMapper.map(it.requireRight())
                } else {
                    null
                }
            } ?: return@get call.respond(HttpStatusCode.InternalServerError, internalServer)

            call.respond(HttpStatusCode.OK, response)
        }

        /**
         * Sets a dns server as selected.
         */
        put("/api/dns") {
            val request =
                kotlin.runCatching { call.receive<DnsRequest>() }.getOrNull() ?: let {
                    return@put call.respond(HttpStatusCode.BadRequest, badRequest)
                }

            putDns(PutDns.Params(request.server)).let {
                if (it.isRight) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    val error = when (it.requireLeft()) {
                        is PutDns.PutDnsFailure -> ErrorWrapper(
                            badRequest,
                            HttpStatusCode.BadRequest
                        )

                        else -> ErrorWrapper(internalServer)
                    }

                    return@put call.respond(error.code, error.error)
                }
            }
        }
    }
}
