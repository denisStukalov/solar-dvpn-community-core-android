package ee.solarlabs.community.core.controllers

import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.registry.tasks.DeleteRegistry
import co.sentinel.dvpn.domain.features.registry.tasks.GetRegistry
import co.sentinel.dvpn.domain.features.registry.tasks.PostRegistry
import ee.solarlabs.community.core.mapper.registry.GetRegistryMapper
import ee.solarlabs.community.core.model.HttpError
import ee.solarlabs.community.core.model.registry.request.PostRegistryRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.java.KoinJavaComponent

fun Application.routeRegistry() {
    val getRegistry: GetRegistry by KoinJavaComponent.inject(GetRegistry::class.java)
    val deleteRegistry: DeleteRegistry by KoinJavaComponent.inject(DeleteRegistry::class.java)
    val postRegistry: PostRegistry by KoinJavaComponent.inject(PostRegistry::class.java)

    routing {
        /**
         * This method is used to retrieve a specific value from local key-value registry.
         */
        get("/api/registry") {
            val key = call.request.queryParameters["key"] ?: let {
                return@get call.respond(HttpStatusCode.BadRequest, HttpError.badRequest)
            }

            getRegistry(GetRegistry.Params(key)).let {
                if (it.isRight) {
                    it.requireRight()?.let { registry ->
                        val response = GetRegistryMapper.map(registry)
                        return@get call.respond(HttpStatusCode.OK, response)
                    } ?: return@get call.respond(HttpStatusCode.NotFound, HttpError.notFound)
                } else {
                    return@get call.respond(
                        HttpStatusCode.InternalServerError,
                        HttpError.internalServer
                    )
                }

            }
        }

        /**
         * This method is used to delete a specific value from local key-value registry.
         */
        delete("/api/registry") {
            val key = call.request.queryParameters["key"] ?: let {
                return@delete call.respond(HttpStatusCode.BadRequest, HttpError.badRequest)
            }

            deleteRegistry(DeleteRegistry.Params(key)).let {
                if (it.isRight) {
                    return@delete call.respond(HttpStatusCode.OK)
                } else {
                    return@delete call.respond(
                        HttpStatusCode.InternalServerError,
                        HttpError.internalServer
                    )
                }
            }
        }

        /**
         * This method is used to set specific value. Overrides, if exists.
         */
        post("/api/registry") {
            val request =
                kotlin.runCatching { call.receive<PostRegistryRequest>() }.getOrNull()
                    ?: let {
                        return@post call.respond(HttpStatusCode.BadRequest, HttpError.badRequest)
                    }

            postRegistry(PostRegistry.Params(request.key, request.value, request.isSecure)).let {
                if (it.isRight) {
                    return@post call.respond(HttpStatusCode.OK)
                } else {
                    return@post call.respond(
                        HttpStatusCode.InternalServerError,
                        HttpError.internalServer
                    )
                }
            }
        }
    }
}
