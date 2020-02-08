package com.wwx.imback.controller

import com.wwx.imback.dto.request.web.WechatAppraiseRequestDto
import com.wwx.imback.dto.request.web.WechatConnectRequestDto
import com.wwx.imback.dto.request.web.WechatSendMsgRequestDto
import com.wwx.imback.service.WechatApiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/wechatApi")
class WechatApiController: AbstractController() {

    @Autowired
    private lateinit var wechatApiService: WechatApiService

    @PostMapping("/connect")
    fun connect(requestDto: WechatConnectRequestDto) = this.wechatApiService.connect(requestDto)

    @PostMapping("/sendMsg")
    fun sendMsg(requestDto: WechatSendMsgRequestDto) = this.wechatApiService.sendMsg(requestDto)

    @PostMapping("/appraise")
    fun appraise(requestDto: WechatAppraiseRequestDto) = this.wechatApiService.appraise(requestDto)
}