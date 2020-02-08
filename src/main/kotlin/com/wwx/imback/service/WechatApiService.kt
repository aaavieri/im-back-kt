package com.wwx.imback.service

import com.wwx.imback.dao.ChannelQuickReplyMapper
import com.wwx.imback.dao.ChatHistoryMapper
import com.wwx.imback.dao.ChatSessionMapper
import com.wwx.imback.dao.UserMapper
import com.wwx.imback.dto.request.web.WechatAppraiseRequestDto
import com.wwx.imback.dto.request.web.WechatConnectRequestDto
import com.wwx.imback.dto.request.web.WechatSendMsgRequestDto
import com.wwx.imback.dto.response.web.WechatConnectResultDto
import com.wwx.imback.dto.response.web.WechatResponseDto
import com.wwx.imback.dto.table.ChatHistoryDto
import com.wwx.imback.dto.table.ChatSessionDto
import com.wwx.imback.dto.table.ClientInfoDto
import com.wwx.imback.error.WechatException
import com.wwx.imback.util.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*

@Service
class WechatApiService {

    @Value("\${app.config.maxSession}")
    private var maxSession: Int = 0

    @Value("\${app.config.maxMessageLength}")
    private var maxMessageLength: Int = 100

    @Autowired
    private lateinit var socketService: SocketService

    @Autowired
    private lateinit var channelQuickReplyMapper: ChannelQuickReplyMapper

    @Autowired
    private lateinit var chatHistoryMapper: ChatHistoryMapper

    @Autowired
    private lateinit var chatSessionMapper: ChatSessionMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    fun connect(requestDto: WechatConnectRequestDto): WechatConnectResultDto {
        val channelQuickReplyDto = this.channelQuickReplyMapper.getUserQuickReplyByType(requestDto.channelID, 1)
        val client = with (requestDto) { ClientInfoDto(channelID, openID, userName, avatar, phoneNum, userStatus) }
        val welcomeMsg = channelQuickReplyDto?.replyContent ?: "嗨~万能的客服小姐姐马上到，我的百倍用心，愿您10分满意！"
        val startTime = Date()
        val latestOpenSession = this.chatSessionMapper.getLatestOpenSession(client.openId)
        val result = WechatConnectResultDto(welcomeMsg = welcomeMsg)
        if (latestOpenSession is ChatSessionDto) {
            val userDto = this.userMapper.getLoginUserById(latestOpenSession.serverUserId, client.channelId)
                    ?: throw WechatException("亲，负责您上次会话的客服人员暂时不在线，请您稍后再试~", 104)
            this.socketService.connect(latestOpenSession, client)
            result.apply {
                sessionId = latestOpenSession.sessionId
                servicerId = userDto.serverUserId
                servicerName = userDto.serverUserName
            }
        } else {
            val onlineUserList = this.userMapper.getLoginUserList(client.channelId)
            if (onlineUserList.isEmpty()) {
                throw WechatException("亲，当前排队人数较多，请您稍后再试~", 101)
            }
            val processingSessionList = this.chatSessionMapper.getMultiUserSessionList(onlineUserList.map { it.serverUserId })
            val processingSessionGroup = processingSessionList.groupBy { it.serverUserId }
            val sortedList = onlineUserList.sortedBy { processingSessionGroup[it.serverUserId]?.size ?: 0 }
            val minSessionUser = sortedList.first()
            if ((processingSessionGroup[minSessionUser.serverUserId]?.size ?: 0) >= this.maxSession) {
                throw WechatException("亲，当前排队人数较多，请您稍后再试~", 102)
            }
            val newSession = ChatSessionDto(0, minSessionUser.serverUserId, client.openId, startTime = startTime)
            try {
                this.chatSessionMapper.addSession(newSession)
            } catch (e: Exception) {
                throw WechatException("对不起，客服会话建立失败，请稍后重试。", 103)
            }
            this.socketService.connect(newSession, client)
            result.apply {
                sessionId = newSession.sessionId
                servicerId = minSessionUser.serverUserId
                servicerName = minSessionUser.serverUserName
            }
        }
        return result
    }

    fun sendMsg(requestDto: WechatSendMsgRequestDto): WechatResponseDto {
        if (requestDto.message.trim().isEmpty() || requestDto.messageType !in (1..3)) {
            throw WechatException("对不起，消息内容不合法。", 201)
        }
        val session = this.chatSessionMapper.getSessionById(requestDto.sessionId)
        if (session == null || session.endTime != null) {
            throw WechatException("对不起，会话不存在或者已过期。", 202)
        }
        val now = Date(System.currentTimeMillis())
        val chatHistoryDto = with (requestDto) { ChatHistoryDto(0, sessionId, message, messageType, 0, createTime = now) }
        val splitChatList = Util.splitMessage(chatHistoryDto, this.maxMessageLength)
        this.chatHistoryMapper.addMessageList(splitChatList)
        this.chatSessionMapper.addMsgCount(requestDto.sessionId, 1, now)
        this.socketService.sendMsg(session, splitChatList.last().apply { message = chatHistoryDto.message })
        return WechatResponseDto()
    }

    fun appraise(requestDto: WechatAppraiseRequestDto): WechatResponseDto {
        if (requestDto.rank !in (1..3)) {
            throw WechatException("没有评分或评分超出范围。", 601)
        }
        val session = this.chatSessionMapper.getSessionById(requestDto.sessionId)
                ?: throw WechatException("找不到评分的客服会话。", 602)
        session.endTime ?: throw WechatException("该会话尚未结束，暂时不能进行评价。", 603)
        if (session.rank in (1..3)) {
            throw WechatException("您已评价过该会话，不能重复评价。", 604)
        }
        this.chatSessionMapper.appraiseSession(requestDto.sessionId, requestDto.rank)
        return WechatResponseDto()
    }
}