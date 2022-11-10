package ee.solarlabs.purchase

import android.app.Activity
import android.content.Context
import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.purchase.model.Offering
import co.sentinel.dvpn.domain.features.purchase.source.PurchaseRepository
import co.sentinel.dvpn.domain.features.purchase.tasks.PostPurchase
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.logInWith
import com.revenuecat.purchases.purchasePackageWith
import ee.solarlabs.purchase.core.exception.PurchaseTaskError
import ee.solarlabs.purchase.core.mapper.OfferingMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

class PurchaseRepositoryImpl(
    private val context: Context
) : PurchaseRepository {

    override suspend fun getOfferings(): Either<Failure, List<Offering>> = kotlin.runCatching {
        getRevenueCatOfferings().let {
            when {
                it.isRight -> {
                    Either.Right(OfferingMapper.map(it.requireRight()))
                }
                else -> Either.Left(it.requireLeft())
            }
        }
    }.onFailure {
        Timber.e(it)
    }.getOrNull() ?: Either.Left(Failure.AppError)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun postPurchase(identifier: String): Either<Failure, Success> =
        kotlin.runCatching {
            getRevenueCatOfferings().let { offeringsResult ->
                offeringsResult.requireRight()
                when {
                    offeringsResult.isRight -> {
                        val offeringPackage =
                            offeringsResult.requireRight().all.flatMap { it.value.availablePackages }
                                .firstOrNull { it.identifier == identifier }
                                ?: return@runCatching Either.Left(PostPurchase.PostPurchaseFailure.PackageIdentifierNotFoundFailure)

                        suspendCancellableCoroutine<Either<Failure, Success>> { coroutine ->
                            Purchases.sharedInstance.purchasePackageWith(
                                context as Activity,
                                offeringPackage,
                                onError = { error, userCancelled ->
                                    coroutine.resume(
                                        if (userCancelled) Either.Left(PostPurchase.PostPurchaseFailure.PurchaseCancelledByUserFailure)
                                        else Either.Left(
                                            PurchaseTaskError(
                                                error.code.code,
                                                error.code.description,
                                                error.underlyingErrorMessage
                                            )
                                        )
                                    )
                                },
                                onSuccess = { _, _ ->
                                    coroutine.resume(Either.Right(Success))
                                }
                            )
                        }
                    }
                    else -> Either.Left(offeringsResult.requireLeft())
                }
            }
        }.onFailure {
            Timber.e(it)
        }.getOrNull() ?: Either.Left(Failure.AppError)

    override suspend fun login(address: String): Either<Failure, Success> = kotlin.runCatching {
        suspendCancellableCoroutine<Either<Failure, Success>> { coroutine ->
            Purchases.sharedInstance.logInWith(address,
                onError = {
                    coroutine.resume(
                        Either.Left(
                            PurchaseTaskError(
                                it.code.code,
                                it.code.description,
                                it.underlyingErrorMessage
                            )
                        )
                    )
                },
                onSuccess = { _, _ ->
                    coroutine.resume(Either.Right(Success))
                })
        }
    }.onFailure {
        Timber.e(it)
    }.getOrNull() ?: Either.Left(Failure.AppError)

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getRevenueCatOfferings(): Either<Failure, com.revenuecat.purchases.Offerings> =
        kotlin.runCatching {
            suspendCancellableCoroutine<Either<Failure, com.revenuecat.purchases.Offerings>> { coroutine ->
                Purchases.sharedInstance.getOfferingsWith(
                    onError = {
                        coroutine.resume(
                            Either.Left(
                                PurchaseTaskError(
                                    it.code.code,
                                    it.code.description,
                                    it.underlyingErrorMessage
                                )
                            )
                        )
                    },
                    onSuccess = {
                        coroutine.resume(Either.Right(it))
                    }
                )
            }
        }.onFailure {
            Timber.e(it)
        }.getOrNull() ?: Either.Left(Failure.AppError)
}