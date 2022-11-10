package co.sentinel.dvpn.hub.tasks

import co.sentinel.cosmos.base.BaseChain
import co.sentinel.cosmos.network.ChannelBuilder
import co.sentinel.cosmos.network.ChannelBuilder.TIME_OUT
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.hub.exception.formatHubTaskError
import cosmos.base.query.v1beta1.Pagination
import kotlinx.coroutines.guava.await
import sentinel.node.v1.Querier
import sentinel.node.v1.QueryServiceGrpc
import sentinel.types.v1.StatusOuterClass
import timber.log.Timber
import java.util.concurrent.TimeUnit

object QueryNodes {
    suspend fun execute(offset: Long = 0, limit: Long = 30, chain: BaseChain) = kotlin.runCatching {
        val stub = QueryServiceGrpc.newFutureStub(ChannelBuilder.getChain(chain))
            .withDeadlineAfter(TIME_OUT.toLong(), TimeUnit.SECONDS)
        val response = stub.queryNodes(
            Querier.QueryNodesRequest.newBuilder()
                .setStatus(StatusOuterClass.Status.STATUS_ACTIVE)
                .setPagination(
                    Pagination.PageRequest.newBuilder()
                        .setLimit(limit)
                        .setOffset(offset)
                ).build()
        ).await()
        Either.Right(response.nodesList)
    }.onFailure { Timber.e(it) }
        .getOrElse {
            Either.Left(formatHubTaskError(it))
        }


    suspend fun execute(chain: BaseChain) = kotlin.runCatching {
        val stub = QueryServiceGrpc.newFutureStub(ChannelBuilder.getChain(chain))
            .withDeadlineAfter(TIME_OUT.toLong(), TimeUnit.SECONDS)
        val response = stub.queryNodes(
            Querier.QueryNodesRequest.newBuilder()
                .setStatus(StatusOuterClass.Status.STATUS_ACTIVE)
                .setPagination(
                    Pagination.PageRequest.newBuilder()
                        .setLimit(10000)
                        .setOffset(0)
                )
                .build()
        ).await()
        Either.Right(response.nodesList)
    }.onFailure { Timber.e(it) }
        .getOrElse {
            Either.Left(formatHubTaskError(it))
        }

}