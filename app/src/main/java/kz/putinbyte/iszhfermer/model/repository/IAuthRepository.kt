package kz.putinbyte.iszhfermer.model.repository

import kz.putinbyte.iszhfermer.entities.network.AuthResponse
import kz.putinbyte.iszhfermer.model.data.server.Result

interface IAuthRepository {

    suspend fun login(
        username: String,
        password: String
    ): Result<AuthResponse>

}