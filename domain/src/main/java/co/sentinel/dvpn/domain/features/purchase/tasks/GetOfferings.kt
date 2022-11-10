package co.sentinel.dvpn.domain.features.purchase.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.features.purchase.model.Offering
import co.sentinel.dvpn.domain.features.purchase.source.PurchaseRepository

class GetOfferings(private val purchaseRepository: PurchaseRepository) {

    suspend operator fun invoke(): Either<Failure, List<Offering>> =
        purchaseRepository.getOfferings()
}