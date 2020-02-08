package com.wwx.imback.clientimport

import com.wwx.imback.converter.FeignDecoder
import com.wwx.imback.converter.FeignEncoder
import com.wwx.imback.util.replaceProperty
import feign.Feign
import feign.Logger
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment

class HttpClientFactoryBean<T>(private val clientInterface: Class<out T>) : FactoryBean<T> {
    @Autowired
    private var environment: Environment? = null
    @Value("\${app.config.feign.logLevel:NONE}")
    private val wholeLogLevel: Logger.Level = Logger.Level.NONE

    @Throws(Exception::class)
    override fun getObject(): T {
        val httpClient = this.clientInterface.getAnnotation(HttpClient::class.java)
        val url: String = httpClient.value.replaceProperty(environment)
        var loggerLevel = httpClient.logLevel
        if (loggerLevel == Logger.Level.NONE && wholeLogLevel != Logger.Level.NONE) {
            loggerLevel = wholeLogLevel
        }
        return Feign.builder()
                .logger(HttpClientLogger(this.clientInterface)).logLevel(loggerLevel)
                .decoder(FeignDecoder(*httpClient.decoderClasses))
                .encoder(FeignEncoder(*httpClient.encoderClasses))
                .target(clientInterface, url)
    }

    override fun getObjectType(): Class<out T> {
        return clientInterface
    }
}