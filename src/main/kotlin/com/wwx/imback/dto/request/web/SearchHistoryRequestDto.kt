package com.wwx.imback.dto.request.web

data class SearchHistoryRequestDto(var serverUserId: Int = 0,
                                   var openId: String = "",
                                   var keyword: String = "")