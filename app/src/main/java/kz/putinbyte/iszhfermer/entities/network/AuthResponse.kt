package kz.putinbyte.iszhfermer.entities.network

import com.google.gson.annotations.SerializedName

class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String?,

    @SerializedName("refreshToken")
    val refreshToken: String?,

    @SerializedName("user")
    val user: String?
)