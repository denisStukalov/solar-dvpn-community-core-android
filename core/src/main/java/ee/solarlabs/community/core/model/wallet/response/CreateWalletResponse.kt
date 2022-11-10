package ee.solarlabs.community.core.model.wallet.response

data class CreateWalletResponse(
    val wallet: Wallet,
    val mnemonic: String // "deer catalog human hint unique grass .... pistol organ toddler"
) {
    data class Wallet(
        val address: String, // "sent09uyu0lrfsdvkeop213jsd7fq2rs29krf25ml87"
        val balance: Int, // 0
        val currency: String // "udvpn"
    )
}