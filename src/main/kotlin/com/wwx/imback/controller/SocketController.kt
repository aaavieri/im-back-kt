package com.wwx.imback.controller

import com.wwx.imback.dto.request.websocket.ServerSendMsgRequestDto
import com.wwx.imback.dto.table.ChatHistoryDto
import com.wwx.imback.error.ApplicationException
import com.wwx.imback.service.SocketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
class SocketController {

    @Autowired
    private lateinit var template: SimpMessagingTemplate

    @Autowired
    private lateinit var socketService: SocketService

    @SubscribeMapping("/serverOn/{serverUserId}")
    @SendTo("/queue")
    fun serverOn(@DestinationVariable("serverUserId") serverUserId: Int): Map<String, Any> {
        this.socketService.serverOn(serverUserId)
        return mapOf()
    }

    @SubscribeMapping("/serverOff/{serverUserId}")
    @SendTo("/queue")
    fun serverOff(@DestinationVariable("serverUserId") serverUserId: Int): Map<String, Any> {
        this.socketService.serverOff(serverUserId)
        return mapOf()
    }

    @SubscribeMapping("/serverSendMsg/{serverUserId}")
    @SendTo("/queue")
    fun serverSendMsg(@DestinationVariable("serverUserId") serverUserId: Int,
                      @RequestBody requestDto: ServerSendMsgRequestDto): ChatHistoryDto {
        return this.socketService.sendToWechat(requestDto)
    }

    @MessageExceptionHandler
    fun handleException(ex: Exception) = when (ex) {
        is ApplicationException -> ex
        else -> ApplicationException(ex.message ?: "未知异常")
    }
}