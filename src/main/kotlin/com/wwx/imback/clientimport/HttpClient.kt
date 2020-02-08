package com.wwx.imback.clientimport

import feign.Logger
import feign.codec.Decoder
import feign.codec.Encoder
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class HttpClient(val value: String = "",
                            val encoderClasses: Array<KClass<out Encoder>> = [GsonEncoder::class],
                            val decoderClasses: Array<KClass<out Decoder>> = [GsonDecoder::class],
                            val logLevel: Logger.Level = Logger.Level.NONE)