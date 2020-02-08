package com.wwx.imback.dto.table

import java.util.*

data class ChatHistoryDto(
        var historyId: Int = 0,
        var sessionId: Int = 0,
        var message: String = "",
        var messageType: Int = 1,
        var type: Int = 0,
        var delFlag: Int = 0,
        var createTime: Date? = null,
        var createUser: String = "",
        var rowVersion: Int = 0
)