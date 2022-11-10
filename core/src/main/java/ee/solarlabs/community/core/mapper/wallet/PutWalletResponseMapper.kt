package ee.solarlabs.community.core.mapper.wallet

import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.wallet.model.Wallet
import ee.solarlabs.community.core.model.wallet.response.PutWalletResponse

object PutWalletResponseMapper : Mapper<Wallet, PutWalletResponse> {
    override fun map(obj: Wallet): PutWalletResponse {
        return PutWalletResponse(
            address = obj.address,
            balance = obj.balance,
            currency = obj.currency
        )
    }
}