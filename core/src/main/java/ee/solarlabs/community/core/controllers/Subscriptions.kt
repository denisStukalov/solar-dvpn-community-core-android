package ee.solarlabs.community.core.controllers

import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.hub.tasks.DeleteSubscription
import co.sentinel.dvpn.domain.features.hub.tasks.GetQuota
import co.sentinel.dvpn.domain.features.hub.tasks.GetSubscriptions
import co.sentinel.dvpn.domain.features.hub.tasks.PostSubscription
import co.sentinel.dvpn.hub.exception.HubTaskError
import ee.solarlabs.community.core.extension.toError
import ee.solarlabs.community.core.mapper.subscriptions.QuotaResponseMapper
import ee.solarlabs.community.core.model.ErrorWrapper
import ee.solarlabs.community.core.model.HttpError.Companion.badRequest
import ee.solarlabs.community.core.model.HttpError.Companion.internalServer
import ee.solarlabs.community.core.model.HttpError.Companion.notFound
import ee.solarlabs.community.core.model.SubscriptionsServiceError.Companion.paymentFailed
import ee.solarlabs.community.core.model.subscriptions.request.SubscriptionRequest
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

fun Application.routeSubscriptions() {
    val getSubscriptions: GetSubscriptions by KoinJavaComponent.inject(GetSubscriptions::class.java)
    val getQuota: GetQuota by KoinJavaComponent.inject(GetQuota::class.java)
    val deleteSubscription: DeleteSubscription by KoinJavaComponent.inject(DeleteSubscription::class.java)
    val postSubscription: PostSubscription by KoinJavaComponent.inject(PostSubscription::class.java)

    routing {
        /**
         * This method is used to retrieve list of addresses of nodes user is subscribed to.
         */
        get("/api/subscriptions") {
            getSubscriptions().let {
                if (it.isRight) {
                    return@get call.respond(HttpStatusCode.OK, it.requireRight())
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is HubTaskError -> ErrorWrapper(failure.toError())
                        else -> ErrorWrapper(internalServer)
                    }
                    return@get call.respond(error.code, error.error)
                }
            }
        }

        /**
         * This method is used to get quota for a specific node.
         */
        get("/api/subscriptions/{node_address}") {
            val param = call.parameters["node_address"] ?: let {
                return@get call.respond(HttpStatusCode.BadRequest, badRequest)
            }

            getQuota(GetQuota.Params(param)).let {
                if (it.isRight) {
                    return@get call.respond(
                        HttpStatusCode.OK,
                        QuotaResponseMapper.map(it.requireRight())
                    )
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is HubTaskError -> ErrorWrapper(failure.toError())
                        is GetQuota.GetQuotaFailure.SubscriptionNotFound -> ErrorWrapper(
                            notFound,
                            HttpStatusCode.NotFound
                        )

                        else -> ErrorWrapper(internalServer)
                    }

                    return@get call.respond(error.code, error.error)
                }
            }
        }

        /**
         * This method is used to subscribe to a node (buy data allowance).
         */
        post("/api/subscriptions") {
            val params =
                kotlin.runCatching { call.receive<SubscriptionRequest>() }.getOrNull() ?: let {
                    return@post call.respond(HttpStatusCode.BadRequest, badRequest)
                }
            postSubscription(
                PostSubscription.Params(
                    nodeAddress = params.nodeAddress,
                    amount = params.amount,
                    currency = params.currency
                )
            ).let {
                if (it.isRight) {
                    return@post call.respond(HttpStatusCode.OK)
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is HubTaskError -> ErrorWrapper(
                            failure.toError(),
                            HttpStatusCode.Unauthorized
                        )

                        is PostSubscription.PostSubscriptionFailure.PaymentRequiredFailure -> ErrorWrapper(
                            paymentFailed,
                            HttpStatusCode.PaymentRequired
                        )

                        else -> ErrorWrapper(internalServer)
                    }

                    return@post call.respond(error.code, error.error)
                }
            }
        }

        /**
         * This method is used to unsubscribe from a node.
         */
        delete("/api/subscriptions") {
            val param = call.request.queryParameters["node_address"] ?: let {
                return@delete call.respond(HttpStatusCode.BadRequest, badRequest)
            }

            deleteSubscription(DeleteSubscription.Params(param)).let {
                if (it.isRight) {
                    return@delete call.respond(HttpStatusCode.OK)
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is HubTaskError -> ErrorWrapper(
                            failure.toError(),
                            HttpStatusCode.Unauthorized
                        )

                        is DeleteSubscription.DeleteSubscriptionFailure.NodeNotFound -> ErrorWrapper(
                            notFound,
                            HttpStatusCode.NotFound
                        )

                        else -> ErrorWrapper(internalServer)
                    }

                    return@delete call.respond(error.code, error.error)
                }
            }

        }

    }
}
