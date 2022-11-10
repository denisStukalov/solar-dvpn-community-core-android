package co.sentinel.dvpn.domain.features.purchase.source

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.purchase.model.Offering

interface PurchaseRepository {

    suspend fun getOfferings(): Either<Failure, List<Offering>>

    suspend fun postPurchase(identifier: String): Either<Failure, Success>

    suspend fun login(address: String): Either<Failure, Success>
}