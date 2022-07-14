package kz.putinbyte.iszhfermer.di.providers

import android.content.SharedPreferences
import com.google.gson.Gson
import iszhfermer.BuildConfig
import kz.putinbyte.iszhfermer.di.ApiPath
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kz.putinbyte.iszhfermer.model.data.server.ApiClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    @ApiPath private val ApiPath: String
) : Provider<ApiClient> {

    override fun get(): ApiClient =
        Retrofit.Builder()
            .baseUrl(ApiPath)
            .client(http())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiClient::class.java)

    private val connectTimeout = 60L
    private val readTimeout = 60L

    fun http(): OkHttpClient = with(OkHttpClient.Builder()) {
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
