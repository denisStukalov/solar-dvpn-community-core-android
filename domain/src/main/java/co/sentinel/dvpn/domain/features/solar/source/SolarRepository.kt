package co.sentinel.dvpn.domain.features.solar.source

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.features.solar.model.request.GetCountriesByContinentRequest
import co.sentinel.dvpn.domain.features.solar.model.request.GetNodesByAddressRequest
import co.sentinel.dvpn.domain.features.solar.model.request.GetNodesRequest
import co.sentinel.dvpn.domain.features.solar.model.result.FetchContinentsResult
import co.sentinel.dvpn.domain.features.solar.model.result.FetchCountriesResult
import co.sentinel.dvpn.domain.features.solar.model.result.FetchNodesResult

interface SolarRepository {
    /**
     * Fetch list of nodes.
     */
    suspend fun fetchNodes(request: GetNodesRequest?): Either<Failure, FetchNodesResult>

    /**
     * Fetch list of nodes by their address.
     */
    suspend fun fetchNodesByAddress(request: GetNodesByAddressRequest): Either<Failure, FetchNodesResult>

    /**
     * Fetch a list of countries for DVPN.
     */
    suspend fun fetchCountries(): Either<Failure, FetchCountriesResult>

    /**
     * Fetch list of continents with their node count.
     */
    suspend fun fetchContinents(): Either<Failure, FetchContinentsResult>

    /**
     * Fetch list of countries by continent with their node count.
     */
    suspend fun fetchCountriesByContinent(request: GetCountriesByContinentRequest): Either<Failure, FetchCountriesResult>
}