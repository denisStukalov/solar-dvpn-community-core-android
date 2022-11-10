package ee.solarlabs.community.core.model.wallet.response

data class GetWalletResponse(
    val address: String, // "sent09uyu0lrfsdvkeop213jsd7fq2rs29krf25ml87"
    val balance: Int, // 0
    val currency: String // "udvpn"
)