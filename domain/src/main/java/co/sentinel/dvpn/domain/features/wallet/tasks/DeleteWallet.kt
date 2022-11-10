package co.sentinel.dvpn.domain.features.wallet.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.interactor.Success
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class DeleteWallet(
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(): Either<Failure, Success> {
        walletRepository.clearWallet()
        return Either.Right(Success)
    }
}