package com.wwx.imback.util

import com.wwx.imback.dto.response.web.SplitKeywordDto
import com.wwx.imback.dto.table.ChatHistoryDto
import kotlin.math.max

object Util {

    fun combineMessage(messageList: List<ChatHistoryDto>) = messageList
            .groupBy { "${it.sessionId}|${it.messageType}|${it.type}|${it.createTime?.time ?: 0L}" }
            .map { it.value.reduce { h1, h2 -> h1.apply { h1.message += h2.message } } }

    fun splitByWords(message: String, delimiters: List<String>): List<SplitKeywordDto> {
        var result = listOf(SplitKeywordDto(false, message))
        delimiters.forEach { delimiter -> result = result.map { splitByWord(it.content, delimiter) }.flatten() }
        return result
    }

    fun splitMessage(messageDto: ChatHistoryDto, maxMessageLength: Int): List<ChatHistoryDto> {
        var message = messageDto.message
        val splitResult = mutableListOf<String>()
        while (maxMessageLength > 0 && message.length > maxMessageLength) {
            splitResult.add(message.substring(0, maxMessageLength))
            message = message.substring(maxMessageLength)
        }
        splitResult.add(message)
        return splitResult.map { messageDto.copy(message = it) }
    }

    fun splitByWord(message: String, delimiter: String): List<SplitKeywordDto> {
        val list = mutableListOf<SplitKeywordDto>()
        var temp = message
        var i = temp.indexOf(delimiter)
        while (i >= 0) {
            list.add(SplitKeywordDto(false, temp.substring(0, i)))
            list.add(SplitKeywordDto(true, delimiter))
            temp = temp.substring(i + delimiter.length)
            i = temp.indexOf(delimiter)
        }
        list.add(SplitKeywordDto(false, temp))
        return list.filter { it.content.isNotEmpty() }
    }
}