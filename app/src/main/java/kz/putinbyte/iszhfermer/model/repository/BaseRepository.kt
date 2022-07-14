package kz.putinbyte.iszhfermer.model.repository

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import kz.putinbyte.iszhfermer.extensions.tryOrNull
import kz.putinbyte.iszhfermer.model.data.server.ApiErrorResponse
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.model.data.server.ServerError
import kz.putinbyte.iszhfermer.utils.GsonUtils
import kz.putinbyte.iszhfermer.utils.LogUtils
import java.io.IOException

/**
 * Project Truck Crew
 * Package kz.putinbyte.iszhfermer.model.repository
 *
 * BaseRepository for the project.
 *
 * Created by Artem Skopincev (aka sharpyx) 12.08.2020
 * Copyright © 2020 TKOInform. All rights reserved.
 */
abstract class BaseRepository {

    /**
     * Call server api method from Retrofit API Interface. Works with suspend functions.
     * @param dispatcher Dispatcher for suspend function calling
     * @param apiCall lambda for suspend function
     * @return Result.Success<T> or Result.Error in case of error api response.
     * @see Result
     */
    protected suspend fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall: suspend () -> T
    ): Result<T> {
        return withContext(dispatcher) {
            try {
                Result.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> Result.Error(throwable)
                    is HttpException -> {
                        val serverError = convertErrorBody(throwable)
                        Result.Error(serverError)
                    }
                    else -> {
                        Result.Error(throwable)
                    }
                }
            }
        }
    }

    /**
     * Read error body from HttpException and returns ServerError.
     * Using GSON to convert json body to class instance.
     * @param throwable [HttpException] from okHttp
     * @return [ServerError]
     */
    private fun convertErrorBody(throwable: HttpException): ServerError {
        try {
            throwable.response()?.errorBody()?.source()?.let {
                val code = throwable.code()
                val resString = it.readUtf8()
                val response = try {
                    Gson().fromJson(resString, ApiErrorResponse::class.java)
                }catch (e: Exception){
                    LogUtils.error(javaClass.simpleName, e.message)
                    ApiErrorResponse(message = resString)
                }
                return ServerError(code, response)
            }

            return ServerError(throwable.code(), null)
        } catch (exception: Exception) {
            return ServerError(throwable.code(), null)
        }
    }
}
