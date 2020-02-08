package com.wwx.imback.service

import com.wwx.imback.client.WwxClient
import com.wwx.imback.dao.ChatHistoryMapper
import com.wwx.imback.dao.ChatSessionMapper
import com.wwx.imback.dto.request.websocket.ServerSendMsgRequestDto
import com.wwx.imback.dto.response.websocket.WechatConnectSendDto
import com.wwx.imback.dto.response.websocket.WechatMessageSendDto
import com.wwx.imback.dto.table.ChatHistoryDto
import com.wwx.imback.dto.table.ChatSessionDto
import com.wwx.imback.dto.table.ClientInfoDto
import com.wwx.imback.util.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.collections.HashSet


@Service
class SocketService {

    private lateinit var template: SimpMessagingTemplate

    @Value("\${app.config.fileAccessPath}")
    private var fileAccessPath: String = ""

    @Value("\${app.config.fileSavePath}")
    private var fileSavePath: String = ""

    @Value("\${app.config.maxMessageLength}")
    private var maxMessageLength: Int = 100

    @Autowired
    private lateinit var wwxClient: WwxClient

    @Autowired
    private lateinit var chatHistoryMapper: ChatHistoryMapper

    @Autowired
    private lateinit var chatSessionMapper: ChatSessionMapper

    private val serverUserSet = HashSet<Int>()

    fun serverOn(serverUserId: Int) = this.serverUserSet.add(serverUserId)

    fun serverOff(serverUserId: Int) = this.serverUserSet.remove(serverUserId)

    @Transactional
    fun sendToWechat(requestDto: ServerSendMsgRequestDto): ChatHistoryDto {
        val convertMessage = when (requestDto.messageType) {
            1 -> requestDto.message.replace(this.fileAccessPath, this.fileSavePath)
            else -> requestDto.message
        }
        this.wwxClient.sendMsg(requestDto.sessionId, requestDto.messageType, convertMessage)
        val now = Date(System.currentTimeMillis())
        val chatHistoryDto = with (requestDto) { ChatHistoryDto(0, sessionId, message, messageType, 1, createTime = now) }
        val splitChatList = Util.splitMessage(chatHistoryDto, this.maxMessageLength)
        this.chatHistoryMapper.addMessageList(splitChatList)
        this.chatSessionMapper.addMsgCount(requestDto.sessionId, 1, now)
        return splitChatList.last().apply { message = chatHistoryDto.message }
    }

    fun connect(session: ChatSessionDto, client: ClientInfoDto) {
        this.template.convertAndSend("/queue/wechatConnect/${session.serverUserId}", WechatConnectSendDto(session, client))
    }

    fun sendMsg(session: ChatSessionDto, history: ChatHistoryDto) {
        this.template.convertAndSend("/queue/wechatSendMsg/${session.serverUserId}", WechatMessageSendDto(session, history))
    }
}