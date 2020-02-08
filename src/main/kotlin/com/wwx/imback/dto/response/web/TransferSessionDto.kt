package com.wwx.imback.dto.response.web

import com.wwx.imback.dto.table.ChatHistoryDto
import com.wwx.imback.dto.table.ChatSessionDto
import com.wwx.imback.dto.table.ClientInfoDto
import com.wwx.imback.dto.table.UserDto

data class TransferSessionDto(
        var clientInfo: ClientInfoDto,
        var serverInfo: UserDto? = null,
        var sessionInfo: ChatSessionDto,
        var msgList: List<ChatHistoryDto> = listOf()
)