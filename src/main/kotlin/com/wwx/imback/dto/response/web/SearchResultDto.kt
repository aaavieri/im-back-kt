package com.wwx.imback.dto.response.web

import com.wwx.imback.dto.table.ChatHistoryDto
import com.wwx.imback.dto.table.ClientInfoDto

data class SearchResultDto(
        var history: ChatHistoryDto,
        var displayTime: String = "",
        var client: ClientInfoDto,
        var messageList: List<SplitKeywordDto> = listOf()
)