package ee.solarlabs.community.core.controllers

import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.wallet.tasks.DeleteWallet
import co.sentinel.dvpn.domain.features.wallet.tasks.GetWallet
import co.sentinel.dvpn.domain.features.wallet.tasks.PostWallet
import co.sentinel.dvpn.domain.features.wallet.tasks.PutWallet
import co.sentinel.dvpn.hub.exception.HubTaskError
import ee.solarlabs.community.core.extension.toError
import ee.solarlabs.community.core.mapper.wallet.CreateWalletResponseMapper
import ee.solarlabs.community.core.mapper.wallet.GetWalletResponseMapper
import ee.solarlabs.community.core.mapper.wallet.PutWalletResponseMapper
import ee.solarlabs.community.core.model.ErrorWrapper
import ee.solarlabs.community.core.model.HttpError
import ee.solarlabs.community.core.model.HttpError.Companion.internalServer
import ee.solarlabs.community.core.model.wallet.request.PutWalletRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import org.koin.java.KoinJavaComponent.inject

val deleteWallet: DeleteWallet by inject(DeleteWallet::class.java)
val getWallet: GetWallet by inject(GetWallet::class.java)
val postWallet: PostWallet by inject(PostWallet::class.java)
val putWallet: PutWallet by inject(PutWallet::class.java)

fun Application.routeWallet() {

    routing {
        /**
         * Deletes current wallet and data.
         */
        delete("/api/wallet") {
            deleteWallet().let {
                if (it.isRight) {
                    return@delete call.respond(HttpStatusCode.OK)
                } else {
                    return@delete call.respond(HttpStatusCode.InternalServerError, internalServer)
                }
            }
        }

        /**
         * Retrieves wallet information.
         */
        get("/api/wallet") {
            getWallet().let {
                if (it.isRight) {
                    return@get call.respond(
                        HttpStatusCode.OK,
                        GetWalletResponseMapper.map(it.requireRight())
                    )
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
         * Creates a new wallet and generate a mnemonic.
         */
        post("/api/wallet") {
            postWallet().let {
                if (it.isRight) {
                    return@post call.respond(
                        HttpStatusCode.OK,
                        CreateWalletResponseMapper.map(it.requireRight())
                    )
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is HubTaskError -> ErrorWrapper(failure.toError())
                        else -> ErrorWrapper(internalServer)
                    }

                    return@post call.respond(error.code, error.error)
                }
            }
        }

        /**
         * Used to recover a wallet using a seed phrase.
         */
        put("/api/wallet") {
            val params =
                kotlin.runCatching { call.receive<PutWalletRequest>() }.getOrNull() ?: let {
                    return@put call.respond(HttpStatusCode.BadRequest, HttpError.badRequest)
                }
            putWallet(PutWallet.Params(params.mnemonic)).let {
                if (it.isRight) {
                    return@put call.respond(
                        HttpStatusCode.OK,
                        PutWalletResponseMapper.map(it.requireRight())
                    )
                } else {
                    val error = when (val failure = it.requireLeft()) {
                        is HubTaskError -> ErrorWrapper(failure.toError())
                        //is PutWallet.Failure.

                        else -> ErrorWrapper(internalServer)
                    }

                    return@put call.respond(error.code, error.error)
                }
            }
        }
    }
}
