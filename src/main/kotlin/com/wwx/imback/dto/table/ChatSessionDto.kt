package com.wwx.imback.dto.table

import java.util.*

data class ChatSessionDto(
        var sessionId: Int = 0,
        var serverUserId: Int = 0,
        var openId: String = "",
        var startTime: Date? = null,
        var endTime: Date? = null,
        var messageCount: Int = 0,
        var rank: Int = 0,
        var delFlag: Int = 0,
        var createTime: Date? = null,
        var createUser: String = "",
        var rowVersion: Int = 0
        )