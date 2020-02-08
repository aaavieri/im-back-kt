package com.wwx.imback.dto

data class TokenDecodeResultDto(val serverUserId: Int, val payload: Map<String, Any>)