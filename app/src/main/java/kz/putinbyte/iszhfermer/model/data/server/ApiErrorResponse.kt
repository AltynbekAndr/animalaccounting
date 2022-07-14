package kz.putinbyte.iszhfermer.model.data.server

import com.google.gson.annotations.SerializedName

/**
 * Project Animal Accounting
 * Package kz.putinbyte.iszhfermer.model.data.server
 *
 * Entity which returns by server when error occurred.
 *
 * Created by Baryktabasov Ilimjan (barIlim) 25.02.2022
 * Copyright © 2022 PutInByte. All rights reserved.
 */
data class ApiErrorResponse(
    @SerializedName("message")
    val message: String
)