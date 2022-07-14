package kz.putinbyte.iszhfermer.model.data.server

import kz.putinbyte.iszhfermer.entities.network.AuthBody
import kz.putinbyte.iszhfermer.entities.network.AuthResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiClient {
    @POST("/api/identity/login")
    suspend fun login(
        @Body body: AuthBody,
        @Header("Content-Type") content_type: String = "application/json"
    ): AuthResponse
}