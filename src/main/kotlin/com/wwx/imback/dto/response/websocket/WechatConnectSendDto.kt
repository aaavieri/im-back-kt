package com.wwx.imback.dto.response.websocket

import com.wwx.imback.dto.table.ChatSessionDto
import com.wwx.imback.dto.table.ClientInfoDto

data class WechatConnectSendDto(
        var session: ChatSessionDto,
        var client: ClientInfoDto
)