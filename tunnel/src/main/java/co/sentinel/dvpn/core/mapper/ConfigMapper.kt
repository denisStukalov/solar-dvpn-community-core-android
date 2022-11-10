package co.sentinel.dvpn.core.mapper

import co.sentinel.dvpn.domain.core.extension.isNotNullOrEmpty
import co.sentinel.dvpn.domain.core.functional.Mapper
import co.sentinel.dvpn.domain.features.dvpn.model.TunnelConfig
import co.sentinel.dvpn.domain.features.dvpn.model.TunnelInterface
import co.sentinel.dvpn.domain.features.dvpn.model.TunnelPeer
import com.wireguard.config.Config
import com.wireguard.config.Interface
import com.wireguard.config.Peer

object DomainConfigToConfigMapper : Mapper<TunnelConfig, Config> {
    override fun map(obj: TunnelConfig): Config {
        val resolvedPeers: MutableCollection<Peer> = ArrayList()
        obj.peers.forEach {
            resolvedPeers.add(with(it) {
                val builder = Peer.Builder()
                if (allowedIps.isNotEmpty()) builder.parseAllowedIPs(allowedIps)
                if (endpoint.isNotNullOrEmpty()) builder.parseEndpoint(endpoint!!)
                if (persistentKeepAlive.isNotNullOrEmpty()) builder.parsePersistentKeepalive(
                    persistentKeepAlive!!
                )
                if (preSharedKey.isNotNullOrEmpty()) builder.parsePreSharedKey(preSharedKey!!)
                if (publicKey.isNotEmpty()) builder.parsePublicKey(publicKey)
                builder.build()
            })
        }
        return Config.Builder()
            .setInterface(with(obj.tunnelInterface) {
                val builder = Interface.Builder()
                if (addresses.isNotEmpty()) builder.parseAddresses(addresses)
                if (dnsServers.isNotEmpty()) builder.parseDnsServers(dnsServers)
                if (excludedApplications.isNotEmpty()) builder.excludeApplications(
                    excludedApplications
                )
                if (includedApplications.isNotEmpty()) builder.includeApplications(
                    includedApplications
                )
                if (listenPort.isNotEmpty()) builder.parseListenPort(listenPort)
                if (mtu.isNotEmpty()) builder.parseMtu(mtu)
                if (privateKey.isNotNullOrEmpty()) builder.parsePrivateKey(privateKey!!)
                builder.build()
            })
            .addPeers(resolvedPeers)
            .build()
    }
}

object ConfigToDomainConfigMapper : Mapper<Config, TunnelConfig> {
    override fun map(obj: Config) = TunnelConfig(
        tunnelInterface = with(obj.`interface`) {
            TunnelInterface(
                excludedApplications = excludedApplications.toList(),
                includedApplications = includedApplications.toList(),
                addresses = addresses.joinToString(),
                dnsServers = dnsServers.joinToString(),
                listenPort = listenPort.orElse(0).toString(),
                mtu = mtu.orElse(0).toString(),
                privateKey = obj.`interface`.keyPair.privateKey.toBase64(),
                publicKey = obj.`interface`.keyPair.publicKey.toBase64()
            )
        },
        peers = obj.peers.map {
            TunnelPeer(
                allowedIps = it.allowedIps.joinToString(),
                endpoint = it.endpoint.orElse(null)?.toString(),
                persistentKeepAlive = it.persistentKeepalive.orElse(null)?.toString(),
                preSharedKey = it.preSharedKey.orElse(null)?.toBase64(),
                publicKey = it.publicKey.toBase64(),
            )
        }
    )
}