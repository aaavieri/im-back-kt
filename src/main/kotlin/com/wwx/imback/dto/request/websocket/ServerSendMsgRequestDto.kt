package com.wwx.imback.dto.request.websocket

data class ServerSendMsgRequestDto(
        var serverUserId: Int = 0,
        var openId: String = "",
        var sessionId: Int = 0,
        var messageType: Int = 1,
        var message: String = ""
)