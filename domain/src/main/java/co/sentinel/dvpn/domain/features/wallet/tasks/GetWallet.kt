package co.sentinel.dvpn.domain.features.wallet.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.features.wallet.model.Wallet
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class GetWallet(
    private val repository: WalletRepository
) {

    suspend operator fun invoke(): Either<Failure, Wallet> = repository.getWallet()

}