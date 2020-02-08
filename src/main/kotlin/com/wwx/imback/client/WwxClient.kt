package com.wwx.imback.client

import com.wwx.imback.clientimport.HttpClient
import com.wwx.imback.converter.FeignDecoder
import feign.Body
import feign.Headers
import feign.Param
import feign.RequestLine

@HttpClient(value = "\${app.config.wwxApiAddress}", decoderClasses = [FeignDecoder::class])
interface WwxClient {

    @RequestLine("POST /close")
    @Headers("Content-Type: application/json")
    @Body("%7B\"sessionId\": \"{sessionId}\", \"closeMsg\": \"{closeMsg}\"%7D")
    fun closeWwx(@Param("sessionId")sessionId: Int, @Param("closeMsg")closeMsg: String)

    @RequestLine("POST /sendMsg")
    @Headers("Content-Type: application/json")
    @Body("%7B\"sessionId\": \"{sessionId}\", \"messageType\": \"{messageType}, \"message\": \"{message}\"%7D")
    fun sendMsg(@Param("sessionId")sessionId: Int, @Param("messageType")messageType: Int,
                @Param("message")message: String)
}