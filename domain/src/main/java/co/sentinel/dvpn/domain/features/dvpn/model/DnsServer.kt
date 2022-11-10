package co.sentinel.dvpn.domain.features.dvpn.model

import com.google.gson.Gson

data class DnsServer(
    val name: Dns,
    val addresses: String
) {

    override fun equals(other: Any?): Boolean {
        return super.equals(other) || (other is DnsServer && other.name == this.name && other.addresses == this.addresses)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + addresses.hashCode()
        return result
    }

    fun serializeToJsonString(): String {
        return gson.toJson(this, DnsServer::class.java)
    }

    companion object {
        val gson = Gson()

        fun deserializeFromJsonString(serialized: String): DnsServer {
            return gson.fromJson(serialized, DnsServer::class.java)
        }
    }


}