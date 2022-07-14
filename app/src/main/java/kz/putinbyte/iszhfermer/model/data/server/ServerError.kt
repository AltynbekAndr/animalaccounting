package kz.putinbyte.iszhfermer.model.data.server

import java.lang.RuntimeException

/**
 * Project Animal Accounting
 * Package kz.putinbyte.iszhfermer.model.data.server
 *
 * Class displays ServerError.
 * @param errorCode Server response code
 * @param errorResponse API error response
 *
 * Created by Baryktabasov Ilimjan (barIlim) 25.02.2022
 * Copyright © 2022 PutInByte. All rights reserved.
 */
class ServerError(
    val errorCode: Int,
    val errorResponse: ApiErrorResponse?
) : RuntimeException()