package com.wwx.imback.dto.request.web

data class WechatSendMsgRequestDto(
        var sessionId: Int = 0,
        var message: String = "",
        var messageType: Int = 1
)