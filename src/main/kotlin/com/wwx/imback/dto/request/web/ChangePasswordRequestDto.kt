package com.wwx.imback.dto.request.web

data class ChangePasswordRequestDto(
        var password: String = "",
        var oldPassword: String = ""
)