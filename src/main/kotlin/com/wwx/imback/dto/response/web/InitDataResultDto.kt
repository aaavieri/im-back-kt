package com.wwx.imback.dto.response.web

import com.wwx.imback.dto.table.UserDto
import com.wwx.imback.dto.table.UserQuickReplyDto

data class InitDataResultDto(
        var sessionList: List<TransferSessionDto>,
        var server: UserDto,
        var leaveQuickReply: UserQuickReplyDto,
        var commonQuickReplyList: List<UserQuickReplyDto>
)