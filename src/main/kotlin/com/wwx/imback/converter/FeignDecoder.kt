package com.wwx.imback.converter

import feign.FeignException
import feign.Response
import feign.codec.DecodeException
import feign.codec.Decoder
import feign.gson.GsonDecoder
import java.io.IOException
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * 自定义解码器
 * @author yjl
 */
class FeignDecoder @JvmOverloads constructor(vararg decoders: KClass<out Decoder> = arrayOf(GsonDecoder::class)) : Decoder {

    /** 解码器LIST  */
    private val decoderList: List<Decoder> = (if (decoders.isNotEmpty()) decoders else arrayOf<KClass<out Decoder>>(GsonDecoder::class))
            .map { it.createInstance() }.toList()

    /**
     * 解码
     * @param response 响应数据
     * @param type feignClient定义接口的返回数据类型
     */
    @Throws(IOException::class, DecodeException::class, FeignException::class)
    override fun decode(response: Response, type: Type): Any {
        var result: Any? = null
        for (decoder in decoderList) {
            try {
                result = decoder.decode(response, type)
            } catch (e: Exception) { // do nothing
            }
            if (result != null) {
                break
            }
        }
        return result ?: Object()
    }

}