package com.wwx.imback.dto.table

import java.util.*

data class UserDto(
        var serverUserId: Int = 0,
        var serverUserAccount: String = "",
        var channelId: Int = 0,
        var serverUserName: String = "",
        var serverUserPass: String = "",
        var settings: String = "",
        var delFlag: Int = 0,
        var createTime: Date? = null,
        var createUser: String = "",
        var updateTime: Date? = null,
        var updateUser: String = "",
        var rowVersion: Int = 0
)