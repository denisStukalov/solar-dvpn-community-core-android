package co.sentinel.solar.core.api.config

import co.sentinel.dvpn.domain.BuildConfig
import co.sentinel.solar.core.api.DVPNService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ee.solarlabs.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Provide "make" methods to create instances of [DVPNServiceFactory]
 * and its related dependencies, such as OkHttpClient, Gson, etc.
 */
object DVPNServiceFactory {

    fun makeService(): DVPNService =
        Retrofit.Builder()
            .baseUrl(Constants.SOLAR)
            .client(makeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(makeGson()))
            .build()
            .create(DVPNService::class.java)


    private fun makeOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()


    private fun makeGson(): Gson =
        GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()


    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return logging
    }

}