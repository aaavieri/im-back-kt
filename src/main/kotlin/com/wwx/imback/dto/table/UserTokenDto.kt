package com.wwx.imback.dto.table

import java.util.*

data class UserTokenDto(
        var serverUserId: Int = 0,
        var token: String = "",
        var loginTime: Date? = null,
        var expireTime: Date? = null,
        var delFlag: Int = 0,
        var createTime: Date? = null,
        var createUser: String = "",
        var updateTime: Date? = null,
        var updateUser: String = "",
        var rowVersion: Int = 0
)