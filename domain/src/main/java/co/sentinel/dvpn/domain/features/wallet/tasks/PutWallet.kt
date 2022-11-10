package co.sentinel.dvpn.domain.features.wallet.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.features.wallet.model.Wallet
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class PutWallet(private val walletRepository: WalletRepository) {

    data class Params(val mnemonics: String)

    suspend operator fun invoke(params: Params): Either<Failure, Wallet> {
        walletRepository.clearWallet()

        val restoreAccountResult = walletRepository.restoreAccount(params.mnemonics)
        if (restoreAccountResult.isLeft) {
            return Either.Left(restoreAccountResult.requireLeft())
        }

        return walletRepository.getWallet()
    }

    sealed class PutWalletFailure : Failure.FeatureFailure() {
        object EmptyKeywords : PutWalletFailure()
        object InvalidKeywords : PutWalletFailure()
    }
}