package com.wwx.imback.service

import com.wwx.imback.dao.UserMapper
import com.wwx.imback.dao.UserTokenMapper
import com.wwx.imback.dto.TokenDecodeResultDto
import com.wwx.imback.dto.request.web.LoginRequestDto
import com.wwx.imback.dto.table.UserDto
import com.wwx.imback.dto.table.UserTokenDto
import com.wwx.imback.error.ApplicationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsersService {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var userTokenMapper: UserTokenMapper

    fun login(request: LoginRequestDto): Map<String, Any> {
        val userDto = this.userMapper.getUserByAccount(request.userAccount) ?: UserDto()
        if (userDto.serverUserPass != request.password) {
            throw ApplicationException("不存在用户或密码错误")
        }
        val tokenDto = this.jwtService.encodeToken(userDto.serverUserId,
                mapOf("serverUserId" to userDto.serverUserId, "channelId" to userDto.channelId))
        this.userTokenMapper.saveToken(UserTokenDto(userDto.serverUserId, tokenDto.token, expireTime = tokenDto.expireDate))
        return mapOf("userInfo" to userDto.apply { serverUserPass = "" }, "token" to tokenDto.token)
    }

    fun logout(serverUserId: Int) = this.userTokenMapper.deleteToken(serverUserId)

    fun getUserInfoById(serverUserId: Int) = this.userMapper.getUserById(serverUserId)

    fun refreshToken(tokenDecodeResultDto: TokenDecodeResultDto): Map<String, Any> {
        val serverUserId = tokenDecodeResultDto.serverUserId
        val newTokenDto = this.jwtService.encodeToken(serverUserId, tokenDecodeResultDto.payload)
        this.userTokenMapper.saveToken(UserTokenDto(serverUserId, newTokenDto.token, expireTime = newTokenDto.expireDate))
        return mapOf("token" to newTokenDto.token)
    }
}