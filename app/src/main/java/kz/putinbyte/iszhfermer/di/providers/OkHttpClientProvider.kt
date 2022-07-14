package kz.putinbyte.iszhfermer.di.providers

import android.content.SharedPreferences
import iszhfermer.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Provider<OkHttpClient> {

    private val connectTimeout = 60L
    private val readTimeout = 60L

    override fun get(): OkHttpClient = with(OkHttpClient.Builder()) {
        connectTimeout(connectTimeout, TimeUnit.SECONDS)
        readTimeout(readTimeout, TimeUnit.SECONDS)

        addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer " + sharedPreferences.getString("TOKEN", "")
                    ).build()
                return chain.proceed(request)
            }
        })

        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        build()
    }
}