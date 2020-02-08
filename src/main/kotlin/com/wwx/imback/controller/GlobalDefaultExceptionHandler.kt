package com.wwx.imback.controller

import com.wwx.imback.dto.response.web.ResponseDto
import com.wwx.imback.dto.response.web.WechatResponseDto
import com.wwx.imback.error.ApplicationException
import com.wwx.imback.error.WechatException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @Auther: yinjiaolong
 * @Date: 2018/12/31 18:12
 * @Description:
 */
@ControllerAdvice
class GlobalDefaultExceptionHandler {
    @ExceptionHandler(ApplicationException::class)
    @ResponseBody
    fun handleApplicationException(e: ApplicationException): ResponseDto {
        logger.error(e.message, e)
        return ResponseDto(false, e.message ?: "")
    }

    @ExceptionHandler(WechatException::class)
    @ResponseBody
    fun handleWechatException(e: WechatException): WechatResponseDto {
        logger.error(e.message, e)
        return WechatResponseDto(e.errCode, e.errMsg)
    }

    @ExceptionHandler(Throwable::class)
    @ResponseBody
    fun handleException(e: Throwable): ResponseDto {
        logger.error(e.message, e)
        return ResponseDto(false, e.message ?: "")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler::class.java)
    }
}