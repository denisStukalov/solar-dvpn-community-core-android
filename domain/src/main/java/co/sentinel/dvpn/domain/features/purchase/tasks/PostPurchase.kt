package co.sentinel.dvpn.domain.features.purchase.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.purchase.source.PurchaseRepository
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class PostPurchase(
    private val purchaseRepository: PurchaseRepository,
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(params: Params): Either<Failure, Success> {
        val accountResult = walletRepository.getAccount()
        return when {
            accountResult.isRight -> {
                val account = accountResult.requireRight()
                purchaseRepository.login(account.address).let {
                    when {
                        it.isRight -> purchaseRepository.postPurchase(params.packageIdentifier)
                        else -> Either.Left(it.requireLeft())
                    }
                }
            }
            else -> Either.Left(accountResult.requireLeft())
        }
    }

    data class Params(val packageIdentifier: String)

    sealed class PostPurchaseFailure : Failure.FeatureFailure() {
        object PackageIdentifierNotFoundFailure : PostPurchaseFailure()
        object PurchaseCancelledByUserFailure : PostPurchaseFailure()
    }
}