package com.wwx.imback.clientimport

import feign.Request
import feign.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * @Auther: yinjiaolong
 * @Date: 2019/1/3 15:35
 * @Description:
 */
class HttpClientLogger internal constructor(private val logger: Logger) : feign.Logger() {

    @JvmOverloads
    constructor(clazz: Class<*> = HttpClientLogger::class.java) : this(LoggerFactory.getLogger(clazz))

    constructor(name: String) : this(LoggerFactory.getLogger(name))

    override fun logRequest(configKey: String, logLevel: Level, request: Request) {
        if (logger.isDebugEnabled) {
            super.logRequest(configKey, logLevel, request)
        }
    }

    @Throws(IOException::class)
    override fun logAndRebufferResponse(configKey: String,
                                        logLevel: Level,
                                        response: Response,
                                        elapsedTime: Long): Response {
        return if (logger.isDebugEnabled) {
            super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime)
        } else response
    }

    override fun log(configKey: String, format: String, vararg args: Any) { // Not using SLF4J's support for parameterized messages (even though it would be more efficient)
// because it would
// require the incoming message formats to be SLF4J-specific.
        if (logger.isDebugEnabled) {
            logger.debug(String.format(methodTag(configKey) + format, *args))
        }
    }

}