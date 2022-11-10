package co.sentinel.dvpn.domain.features.wallet.tasks

import co.sentinel.dvpn.domain.core.exception.Failure
import co.sentinel.dvpn.domain.core.functional.Either
import co.sentinel.dvpn.domain.core.functional.requireLeft
import co.sentinel.dvpn.domain.core.functional.requireRight
import co.sentinel.dvpn.domain.features.wallet.model.Account
import co.sentinel.dvpn.domain.features.wallet.source.WalletRepository

class PostWallet(private val walletRepository: WalletRepository) {

    suspend operator fun invoke(): Either<Failure, Account> {
        walletRepository.clearWallet()

        val generateKeywordsResult = walletRepository.generateKeywords()
        if (generateKeywordsResult.isLeft) {
            return Either.Left(generateKeywordsResult.requireLeft())
        }

        val keywords = generateKeywordsResult.requireRight().keywords
        val entropy = generateKeywordsResult.requireRight().entropy
        val generateAccountResult = walletRepository.generateAccount(
            entropy,
            keywords
        )
        if (generateAccountResult.isLeft) {
            return Either.Left(generateAccountResult.requireLeft())
        }

        val walletResult = walletRepository.getWallet()
        if (walletResult.isLeft) {
            return Either.Left(walletResult.requireLeft())
        }

        val account = Account(
            wallet = walletResult.requireRight(),
            mnemonics = keywords.joinToString(" ")
        )
        return Either.Right(account)
    }
}