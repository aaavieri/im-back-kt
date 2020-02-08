package com.wwx.imback.controller

import com.wwx.imback.dto.request.web.LoginRequestDto
import com.wwx.imback.dto.response.web.ResponseDto
import com.wwx.imback.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController("/users")
class UsersController: AbstractController() {

    @Autowired
    private lateinit var usersService: UsersService

    @PostMapping("/login")
    @ResponseBody
    fun login(requestDto: LoginRequestDto) = ResponseDto(data = this.usersService.login(requestDto))

    @PostMapping("/checkLogin")
    @ResponseBody
    fun checkLogin(request: HttpServletRequest) = ResponseDto(data = this.decodeToken(request).serverUserId)

    @PostMapping("/logout")
    @ResponseBody
    fun logout(request: HttpServletRequest) = ResponseDto(data = this.usersService.logout(this.decodeToken(request).serverUserId))
}