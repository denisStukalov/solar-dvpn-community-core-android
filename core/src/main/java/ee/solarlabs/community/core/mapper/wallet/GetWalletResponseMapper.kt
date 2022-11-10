package ee.solarlabs.community.core.mapper.wallet

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.wallet.model.Wallet
import ee.solarlabs.community.core.model.wallet.response.GetWalletResponse

object GetWalletResponseMapper : Mapper<Wallet, GetWalletResponse> {
    override fun map(obj: Wallet): GetWalletResponse {
        return GetWalletResponse(
            address = obj.address,
            balance = obj.balance,
            currency = obj.currency
        )
    }
}