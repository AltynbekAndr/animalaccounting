package kz.putinbyte.iszhfermer.model.interactors

import android.content.Context
import android.content.SharedPreferences
import kz.putinbyte.iszhfermer.entities.network.AuthResponse
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.model.repository.AuthRepository
import kz.putinbyte.iszhfermer.model.repository.IAuthRepository
import kz.putinbyte.iszhfermer.utils.LogUtils
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: IAuthRepository,
    private val sharedPreferences: SharedPreferences,
    private val context: Context,
)  {

    val TAG = javaClass.simpleName

    suspend fun login(
        username: String,
        password: String,
    ): Result<AuthResponse> {
        val result =
            authRepository.login(
                username,
                password
            )
        return result
    }

    fun getContext(): Context = context

    suspend fun tokenUpdater(): Result<AuthResponse> {
        return try {
            val userName = sharedPreferences.getString("USERNAME", "")
            val password = sharedPreferences.getString("PASSWORD", "")
            if (!userName.isNullOrEmpty() && !password.isNullOrEmpty()) {
                val result = login(
                    userName,
                    password
                )
                if (result is Result.Success) {
                    saveAuthData(userName, password, result.data.accessToken)
                }
                result
            }
            Result.Error(Exception("Авторизационные данные пусты"))
        } catch (e: Exception) {
            LogUtils.error(TAG, e.message)
            Result.Error(Exception("Произошла ошибка"))
        }
    }

    fun saveAuthData(username: String, password: String, token: String? = "") {
        sharedPreferences.edit().putString("USERNAME", username).apply()
        sharedPreferences.edit().putString("PASSWORD", password).apply()
        sharedPreferences.edit().putString("TOKEN", token).apply()
    }
    fun logout(){
        clearAuthData()
    }
    fun clearAuthData(){
        sharedPreferences.edit().putString("USERNAME", null).apply()
        sharedPreferences.edit().putString("PASSWORD", null).apply()
        sharedPreferences.edit().putString("TOKEN", null).apply()
        sharedPreferences.edit().putString("PIN_CODE", null).apply()
    }
}