package com.wwx.imback.dto.table

import java.util.*

data class ClientInfoDto(
        var channelId: Int = 0,
        var openId: String = "",
        var userName: String = "",
        var avatar: String = "",
        var phoneNum: String = "",
        var userStatus: Int = 1,
        var delFlag: Int = 0,
        var createTime: Date? = null,
        var createUser: String = "",
        var updateTime: Date? = null,
        var updateUser: String = "",
        var rowVersion: Int = 0
)