package co.sentinel.dvpn.hub.tasks

import co.sentinel.cosmos.base.BaseChain
import co.sentinel.cosmos.network.ChannelBuilder
import co.sentinel.cosmos.network.ChannelBuilder.TIME_OUT
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.hub.exception.formatHubTaskError
import kotlinx.coroutines.guava.await
import sentinel.node.v1.Querier
import sentinel.node.v1.QueryServiceGrpc
import timber.log.Timber
import java.util.concurrent.TimeUnit

object QueryNode {
    suspend fun execute(nodeAddress: String, chain: BaseChain) = kotlin.runCatching {
        val stub = QueryServiceGrpc.newFutureStub(ChannelBuilder.getChain(chain))
            .withDeadlineAfter(TIME_OUT.toLong(), TimeUnit.SECONDS)
        val response = stub.queryNode(
            Querier.QueryNodeRequest.newBuilder()
                .setAddress(nodeAddress)
                .build()
        ).await()
        Either.Right(response.node)
    }.onFailure { Timber.e(it) }
        .getOrElse {
            Either.Left(formatHubTaskError(it))
        }
}