package ee.solarlabs.community.core.model.wallet.response

data class PutWalletResponse(val address: String, val balance: Int, val currency: String)
