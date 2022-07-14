package kz.putinbyte.iszhfermer.model.repository

import kz.putinbyte.iszhfermer.entities.network.AuthBody
import kz.putinbyte.iszhfermer.entities.network.AuthResponse
import kz.putinbyte.iszhfermer.model.data.server.AuthApiClient
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiClient: AuthApiClient,
    private val referencesInteractor: ReferencesInteractor
) : IAuthRepository,BaseRepository() {

    override suspend fun login(
        username: String,
        password: String
    ): Result<AuthResponse> {
        referencesInteractor.phoneNumber = username
        return safeApiCall { apiClient.login(body = AuthBody(login = username, password = password)) }
    }

}