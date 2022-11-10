package ee.solarlabs.community.core.controllers

import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.purchase.tasks.GetOfferings
import co.sentinel.dvpn.domain.features.purchase.tasks.PostPurchase
import ee.solarlabs.community.core.extension.toError
import ee.solarlabs.community.core.mapper.purchases.GetOfferingsResponseMapper
import ee.solarlabs.community.core.model.ErrorWrapper
import ee.solarlabs.community.core.model.HttpError
import ee.solarlabs.community.core.model.HttpError.Companion.notFound
import ee.solarlabs.community.core.model.PurchaseError.Companion.purchaseCancelled
import ee.solarlabs.community.core.model.purchases.request.PurchaseRequest
import ee.solarlabs.purchase.core.exception.PurchaseTaskError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.java.KoinJavaComponent

fun Application.routePurchases() {
    val getOfferings: GetOfferings by KoinJavaComponent.inject(GetOfferings::class.java)
    val postPurchase: PostPurchase by KoinJavaComponent.inject(PostPurchase::class.java)

    routing {
        /**
         * This method is used to retrieve list of offerings from RevenueCat.
         */
        get("/api/offerings") {
            getOfferings().let {
                if (it.isRight) {
                    val response = it.requireRight().map { GetOfferingsResponseMapper.map(it) }
                    return@get call.respond(HttpStatusCode.OK, response)
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is PurchaseTaskError -> ErrorWrapper(failure.toError())
                        else -> ErrorWrapper(HttpError.internalServer)
                    }

                    return@get call.respond(error.code, error.error)
                }
            }
        }

        /**
         * This method is used to purchase a product in RevenueCat.
         */
        post("/api/purchase") {
            val request =
                kotlin.runCatching { call.receive<PurchaseRequest>() }.getOrNull()
                    ?: let {
                        return@post call.respond(HttpStatusCode.BadRequest, HttpError.badRequest)
                    }

            postPurchase(PostPurchase.Params(request.packageIdentifier)).let {
                if (it.isRight) {
                    return@post call.respond(HttpStatusCode.OK)
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is PurchaseTaskError -> ErrorWrapper(failure.toError())
                        is PostPurchase.PostPurchaseFailure.PackageIdentifierNotFoundFailure -> ErrorWrapper(
                            notFound, HttpStatusCode.NotFound
                        )

                        is PostPurchase.PostPurchaseFailure.PurchaseCancelledByUserFailure -> ErrorWrapper(
                            purchaseCancelled
                        )

                        else -> ErrorWrapper(HttpError.internalServer)
                    }

                    return@post call.respond(error.code, error.error)
                }
            }
        }
    }
}
