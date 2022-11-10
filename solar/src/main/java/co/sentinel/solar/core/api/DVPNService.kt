package co.sentinel.solar.core.api

import co.sentinel.dvpn.domain.features.solar.model.Country
import co.sentinel.dvpn.domain.features.solar.model.request.GetNodesByAddressRequest
import co.sentinel.solar.core.model.GetNodesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DVPNService {
    @GET("dvpn/getCountries")
    suspend fun getCountries(): List<Country>

    @GET("dvpn/getNodes")
    suspend fun getNodes(
        @Query("continent") continent: String? = null,
        @Query("country") country: String? = null,
        @Query("status") status: String? = null,
        @Query("min_price") minPrice: Int? = null,
        @Query("max_price") maxPrice: Int? = null,
        @Query("handshake_required") handshakeRequired: Boolean? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("page") page: Int? = null,
        @Query("q") query: String? = null
    ): GetNodesResponse

    @POST("dvpn/getNodesByAddress")
    suspend fun getNodesByAddress(@Body request: GetNodesByAddressRequest): GetNodesResponse

}