package com.wwx.imback.dto.table

import java.util.*

data class UserQuickReplyDto(
        var quickReplyId: Int = 0,
        var serverUserId: Int = 0,
        var type: Int = 0,
        var replyContent: String = "",
        var delFlag: Int = 0,
        var createTime: Date? = null,
        var createUser: String = "",
        var updateTime: Date? = null,
        var updateUser: String = "",
        var rowVersion: Int = 0
)