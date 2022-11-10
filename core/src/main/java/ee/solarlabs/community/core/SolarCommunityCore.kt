package ee.solarlabs.community.core

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import co.sentinel.cosmos.core.di.walletModule
import co.sentinel.cosmos.di.cosmosModule
import co.sentinel.dvpn.cache.di.cacheModule
import co.sentinel.dvpn.core.di.tunnelModule
import co.sentinel.dvpn.domain.core.di.domainModule
import co.sentinel.dvpn.domain.features.dvpn.InitBackend
import co.sentinel.dvpn.domain.features.dvpn.LoadTunnels
import co.sentinel.dvpn.domain.features.dvpn.RestoreState
import co.sentinel.dvpn.hub.core.di.hubModule
import co.sentinel.solar.core.di.solarModule
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import ee.solarlabs.community.core.plugins.configureCors
import ee.solarlabs.community.core.plugins.configureMonitoring
import ee.solarlabs.community.core.plugins.configureRouting
import ee.solarlabs.community.core.plugins.configureSerialization
import ee.solarlabs.community.core.plugins.configureSockets
import ee.solarlabs.constants.BaseUrl.Companion.CORE_HOST
import ee.solarlabs.constants.BaseUrl.Companion.CORE_PORT
import ee.solarlabs.constants.BaseUrl.Companion.PURCHASE_API_KEY
import ee.solarlabs.purchase.core.di.purchaseModule
import ee.solarlabs.registry.core.di.registryModule
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import java.util.Locale


class SolarCommunityCore {
    companion object {
        private val TAG = SolarCommunityCore::class.java.simpleName


        private val initBackend: InitBackend by inject(InitBackend::class.java)
        private val restoreState: RestoreState by inject(RestoreState::class.java)
        private val loadTunnels: LoadTunnels by inject(LoadTunnels::class.java)


        /**
         * Entry point to the community core.
         * It is recommended to be called from the application class.
         * @param application must be Application context.
         * @param wait if true, then the start call blocks a current
         * thread until it finishes its execution.
         * If you run start from the main thread with wait = false and nothing else blocking this thread,
         * then your application will be terminated without handling any requests.
         * @throws IllegalArgumentException if context is not application context.
         */
        fun init(application: Context, wait: Boolean) {
            if (application !is Application) {
                throw IllegalArgumentException("Passed context must be application context.")
            }

            initKoin(application)

            initPurchases(application)
            initDvpnBackend(application)
            Timber.plant(Timber.DebugTree())

            GlobalScope.launch {
                embeddedServer(Netty, CORE_PORT, CORE_HOST) {
                    configureCors()
                    configureSockets()
                    configureRouting()
                    configureSerialization()
                    configureMonitoring()
                }.start(wait = wait)
            }

        }

        private fun initPurchases(context: Context) {
            if (PURCHASE_API_KEY.isEmpty()) return
            Purchases.configure(
                PurchasesConfiguration.Builder(context, PURCHASE_API_KEY).build()
            )
        }

        /**
         * Inits the VPN core.
         */
        private fun initDvpnBackend(context: Context) {
            initBackend(MainScope(), InitBackend.InitBackendParams(
                userAgent = String.format(
                    Locale.ENGLISH,
                    "Solar Community Core/%s (Android %d; %s; %s; %s %s; %s)",
                    context.applicationInfo.loadLabel(context.packageManager).toString(),
                    Build.VERSION.SDK_INT,
                    if (Build.SUPPORTED_ABIS.isNotEmpty()) Build.SUPPORTED_ABIS[0] else "unknown ABI",
                    Build.BOARD,
                    Build.MANUFACTURER,
                    Build.MODEL,
                    Build.FINGERPRINT
                )
            ) {
                handleAlwaysOn()
            }) {
                it.fold({ fail ->
                    Log.i(TAG, "Failed to start up VPN Backend, failure: $fail")
                }, {
                    loadTunnels(MainScope())
                })
            }
        }

        /**
         * Restores former state if any.
         */
        private fun handleAlwaysOn() {
            restoreState(MainScope(), RestoreState.RestoreStateParams(true))
        }

        private fun initKoin(application: Context) {
            startKoin {
                androidContext(application)
                modules(
                    cacheModule,
                    tunnelModule,
                    cosmosModule,
                    walletModule,
                    hubModule,
                    solarModule,
                    domainModule,
                    purchaseModule,
                    registryModule
                )
            }
        }
    }
}