package com.wwx.imback.service

import com.wwx.imback.client.WwxClient
import com.wwx.imback.dao.*
import com.wwx.imback.dto.request.web.SaveQuickReplyRequestDto
import com.wwx.imback.dto.response.web.InitDataResultDto
import com.wwx.imback.dto.response.web.SearchResultDto
import com.wwx.imback.dto.response.web.TransferSessionDto
import com.wwx.imback.dto.table.UserQuickReplyDto
import com.wwx.imback.error.ApplicationException
import com.wwx.imback.util.DateUtils
import com.wwx.imback.util.Util
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ServerApiService {

    @Autowired
    private lateinit var channelQuickReplyMapper: ChannelQuickReplyMapper

    @Autowired
    private lateinit var chatSessionMapper: ChatSessionMapper

    @Autowired
    private lateinit var chatHistoryMapper: ChatHistoryMapper

    @Autowired
    private lateinit var clientInfoMapper: ClientInfoMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var userQuickReplyMapper: UserQuickReplyMapper

    @Autowired
    private lateinit var wwxClient: WwxClient

    fun getInitData(serverUserId: Int): InitDataResultDto {
        val userSessionList = this.chatSessionMapper.getUserSessionList(serverUserId).distinctBy { it.sessionId }
        val quickReplyMap = this.userQuickReplyMapper.getUserQuickReply(serverUserId).groupBy { it.type }
        val historyList = Util.combineMessage(this.chatHistoryMapper.getHistoryBySessionList(userSessionList.map { it.sessionId }))
        val clientList = this.clientInfoMapper.getClientList(userSessionList.map { it.openId })
        val serverUser = this.userMapper.getUserById(serverUserId) ?: throw ApplicationException("不存在的用户")
        val sessionList = userSessionList.map { session ->
            val client = clientList.find { it.openId == session.openId }
                    ?: throw ApplicationException("不存在的openId:${session.openId}")
            TransferSessionDto(client, null, session,
                    historyList.filter { it.sessionId == session.sessionId })
        }
        return InitDataResultDto(sessionList, serverUser,
                quickReplyMap[1]?.get(0) ?: UserQuickReplyDto(replyContent = "客服暂时离开，请稍后。"),
                quickReplyMap[0] ?: listOf())
    }

    fun getMoreMsg(openId: String, sessionId: Int): TransferSessionDto {
        val beforeSession = this.chatSessionMapper.getSessionBeforeOne(openId, sessionId) ?: throw ApplicationException("没有更多记录了")
        val historyList = Util.combineMessage(this.chatHistoryMapper.getHistoryBySessionId(sessionId))
        val client = this.clientInfoMapper.getClientByOpenId(openId) ?: throw ApplicationException("客户不存在或已被清空")
        val serverUser = this.userMapper.getUserById(beforeSession.serverUserId) ?: throw ApplicationException("之前服务的客服不存在或已被清空")
        return TransferSessionDto(client, serverUser, beforeSession, historyList)
    }

    fun searchHistory(serverUserId: Int, openId: String, keyword: String): List<SearchResultDto> {
        if (keyword.trim() == "") throw ApplicationException("搜索的关键字为空")
        val sessionList = when (openId) {
            "" -> this.chatSessionMapper.getUserSessionList(serverUserId)
            else -> this.chatSessionMapper.getUserClientSessionList(serverUserId, openId)
        }
        val keywordList = keyword.split(" ")
        val historyList = Util.combineMessage(this.chatHistoryMapper.getHistoryBySessionAndWord(sessionList.map { it.sessionId },
                keywordList.map { "%${it}%" }))
        val clientList = this.clientInfoMapper.getClientList(sessionList.map { it.openId })
        val clientMap = mapOf(*(clientList.map { it.openId to it }.toTypedArray()))
        val sessionMap = mapOf(*(sessionList.map{ it.sessionId to it }.toTypedArray()))
        return historyList.map {
            SearchResultDto(it, DateUtils.formatDate(DateUtils.YYYYMMDD_HHMMSS, it.createTime!!),
                    clientMap[sessionMap[it.sessionId]?.openId]!!, Util.splitByWords(it.message, keywordList))
        }
    }

    fun close(sessionId: Int, channelId: Int) {
        val closeMsg = this.channelQuickReplyMapper.getUserQuickReplyByType(channelId, 2)?.replyContent
                ?: "我的百倍用心，愿您10分满意！期待与您的下次相遇~"
        this.wwxClient.closeWwx(sessionId, closeMsg)
        this.chatSessionMapper.closeSession(sessionId)
    }

    fun saveQuickReply(requestDto: SaveQuickReplyRequestDto, serverUserId: Int): Int {
        with (requestDto) {
            val replyDto = when (requestDto.quickReplyId) {
                0 -> UserQuickReplyDto(serverUserId = serverUserId, type = type, replyContent = replyContent)
                else -> UserQuickReplyDto(requestDto.quickReplyId, type = type, replyContent = replyContent)
            }
            if (requestDto.quickReplyId == 0) {
                userQuickReplyMapper.insertQuickReply(replyDto)
            } else {
                userQuickReplyMapper.updateQuickReply(replyDto)
            }
            return replyDto.quickReplyId
        }
    }

    fun changePassword(serverUserId: Int, password: String, oldPassword: String) {
        val serverUser = this.userMapper.getUserById(serverUserId)
        if (serverUser?.serverUserPass != oldPassword) {
            throw ApplicationException("原密码错误")
        }
        this.userMapper.changePassword(serverUserId, password)
    }
}