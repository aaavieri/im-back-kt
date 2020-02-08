package com.wwx.imback.dto.response.web

data class ResponseDto(var success: Boolean = true,
                       var errMsg: String = "",
                       var data: Any = emptyMap<String, Any>())