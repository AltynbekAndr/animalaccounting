package kz.putinbyte.iszhfermer.di.providers


import com.google.gson.Gson
import iszhfermer.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kz.putinbyte.iszhfermer.di.ApiPath
import kz.putinbyte.iszhfermer.model.data.server.AuthApiClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class AuthApiProvider @Inject constructor(
    private val gson: Gson,
    @ApiPath private val ApiPath: String
) : Provider<AuthApiClient> {

    override fun get(): AuthApiClient =
        Retrofit.Builder()
            .baseUrl(ApiPath)
            .client(http())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuthApiClient::class.java)

    private val connectTimeout = 60L
    private val readTimeout = 60L

    fun http(): OkHttpClient = with(OkHttpClient.Builder()) {
        connectTimeout(connectTimeout, TimeUnit.SECONDS)
        readTimeout(readTimeout, TimeUnit.SECONDS)

        addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request().newBuilder()
                    .addHeader(
                        "VerificationCode",
                        "3d0e65dfe82d2ab6ae3def37a3342a682f61a9be00bbbe052e4b0ae837ed8464"
                    ).addHeader(
                        "Content-Type",
                        "application/json"
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
