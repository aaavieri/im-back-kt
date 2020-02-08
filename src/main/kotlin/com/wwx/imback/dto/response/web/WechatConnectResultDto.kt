package com.wwx.imback.dto.response.web

data class WechatConnectResultDto(
        var sessionId: Int = 0,
        var servicerId: Int = 0,
        var servicerName: String = "",
        var welcomeMsg: String = ""
)