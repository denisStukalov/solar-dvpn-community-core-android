package ee.solarlabs.community.core.mapper.wallet

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.wallet.model.Account
import ee.solarlabs.community.core.model.wallet.response.CreateWalletResponse

object CreateWalletResponseMapper : Mapper<Account, CreateWalletResponse> {
    override fun map(obj: Account): CreateWalletResponse {
        return CreateWalletResponse(
            wallet = CreateWalletResponse.Wallet(
                address = obj.wallet.address,
                balance = obj.wallet.balance,
                currency = obj.wallet.currency
            ),
            mnemonic = obj.mnemonics
        )
    }
}