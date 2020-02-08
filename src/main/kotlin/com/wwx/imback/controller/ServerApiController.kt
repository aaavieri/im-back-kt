package com.wwx.imback.controller

import com.wwx.imback.dto.request.web.*
import com.wwx.imback.dto.response.web.ResponseDto
import com.wwx.imback.error.ApplicationException
import com.wwx.imback.service.ServerApiService
import com.wwx.imback.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController("/serverApi")
class ServerApiController: AbstractController() {

    @Autowired
    private lateinit var serverApiService: ServerApiService

    @Autowired
    private lateinit var usersService: UsersService

    @GetMapping("/getUserInfoByToken")
    @ResponseBody
    fun getUserInfoByToken(request: HttpServletRequest)
            = ResponseDto(data = this.usersService.getUserInfoById(this.decodeToken(request).serverUserId)
            ?: throw ApplicationException("不存在的用户"))

    @PostMapping("/refreshToken")
    @ResponseBody
    fun refreshToken(request: HttpServletRequest)
            = ResponseDto(data = this.usersService.refreshToken(this.decodeToken(request)))

    @GetMapping("/getInitData")
    @ResponseBody
    fun getInitData(request: HttpServletRequest)
            = ResponseDto(data = this.serverApiService.getInitData(this.decodeToken(request).serverUserId))

    @PostMapping("/moreMsg")
    @ResponseBody
    fun moreMsg(requestDto: MoreMsgRequestDto)
            = ResponseDto(data = this.serverApiService.getMoreMsg(requestDto.openId, requestDto.sessionId))

    @PostMapping("/searchHistory")
    @ResponseBody
    fun searchHistory(requestDto: SearchHistoryRequestDto)
            = ResponseDto(data = this.serverApiService.searchHistory(requestDto.serverUserId, requestDto.openId, requestDto.keyword))

    @PostMapping("/close")
    @ResponseBody
    fun close(requestDto: CloseRequestDto, request: HttpServletRequest): ResponseDto {
        this.serverApiService.close(requestDto.sessionId, this.decodeToken(request).payload["channelId"] as Int)
        return ResponseDto(data = emptyMap<String, Any>())
    }

    @PostMapping("/saveQuickReply")
    @ResponseBody
    fun saveQuickReply(requestDto: SaveQuickReplyRequestDto, request: HttpServletRequest)
            = ResponseDto(data = this.serverApiService.saveQuickReply(requestDto, this.decodeToken(request).serverUserId))

    @PostMapping("/changePassword")
    @ResponseBody
    fun changePassword(requestDto: ChangePasswordRequestDto, request: HttpServletRequest)
            = ResponseDto(data = this.serverApiService.changePassword(this.decodeToken(request).serverUserId,
            requestDto.password, requestDto.oldPassword))
}