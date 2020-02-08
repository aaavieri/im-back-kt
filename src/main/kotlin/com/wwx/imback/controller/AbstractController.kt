package com.wwx.imback.controller

import com.wwx.imback.service.JwtService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest

abstract class AbstractController {

    @Autowired
    private lateinit var jwtService: JwtService

    fun decodeToken(request: HttpServletRequest) = this.jwtService.decodeToken(request.getHeader("token"))

    fun encodeToken(serverUserId: Int, payload: Map<String, Any>) = this.jwtService.encodeToken(serverUserId, payload)

    protected fun getLogger(): Logger = logger

    companion object {
        private val logger = LoggerFactory.getLogger(this.javaClass)
    }
}