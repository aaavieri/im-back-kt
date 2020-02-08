package com.wwx.imback.dto.request.web

data class WechatConnectRequestDto(
        var openID: String = "",
        var channelID: Int = 1,
        var userStatus: Int = 1,
        var userName: String = "",
        var avatar: String = "",
        var phoneNum: String = ""
)