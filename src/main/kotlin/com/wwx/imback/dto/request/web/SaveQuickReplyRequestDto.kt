package com.wwx.imback.dto.request.web

data class SaveQuickReplyRequestDto(
        var serverUserId: Int = 0,
        var type: Int = 0,
        var quickReplyId: Int = 0,
        var replyContent: String = ""
)