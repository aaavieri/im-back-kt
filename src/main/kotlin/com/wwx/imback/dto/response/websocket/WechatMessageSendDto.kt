package com.wwx.imback.dto.response.websocket

import com.wwx.imback.dto.table.ChatHistoryDto
import com.wwx.imback.dto.table.ChatSessionDto

data class WechatMessageSendDto(
        var session: ChatSessionDto,
        var history: ChatHistoryDto
)