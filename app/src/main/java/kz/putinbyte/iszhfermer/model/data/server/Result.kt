package kz.putinbyte.iszhfermer.model.data.server

/**
 * Project Animal Accounting
 * Package kz.putinbyte.iszhfermer.model.data.server
 *
 * Wrapper for server result.
 *
 * Created by Baryktabasov Ilimjan (barIlim) 25.02.2022
 * Copyright © 2022 PutInByte. All rights reserved.
 */

sealed class Result<out R : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}