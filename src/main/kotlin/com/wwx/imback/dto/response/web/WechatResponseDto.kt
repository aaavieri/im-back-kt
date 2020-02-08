package com.wwx.imback.dto.response.web

data class WechatResponseDto(
        var errCode: Int = 0,
        var errMsg: String = "",
        var data: Any = mapOf<String, Any>()
)