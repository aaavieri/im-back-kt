package com.wwx.imback.converter

import feign.RequestTemplate
import feign.codec.EncodeException
import feign.codec.Encoder
import feign.gson.GsonEncoder
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * 自定义解码器
 * @author yjl
 */
class FeignEncoder @JvmOverloads constructor(vararg encoders: KClass<out Encoder> = arrayOf(GsonEncoder::class)) : Encoder {
    /** 解码器LIST  */
    private val encoderList: List<Encoder> = (if (encoders.isNotEmpty()) encoders else arrayOf(GsonEncoder::class))
            .map { it.createInstance() }.toList()

    @Throws(EncodeException::class)
    override fun encode(o: Any, type: Type, requestTemplate: RequestTemplate) {
        for (encoder in encoderList) {
            try {
                encoder.encode(o, type, requestTemplate)
                break
            } catch (e: Exception) { // do nothing
            }
        }
    }
}