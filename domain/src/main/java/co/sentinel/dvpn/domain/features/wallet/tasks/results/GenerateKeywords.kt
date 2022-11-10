package co.sentinel.dvpn.domain.features.wallet.tasks.results

class GenerateKeywords {
    data class Success(val keywords: List<String>, val address: String, val entropy: String)
}