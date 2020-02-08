package com.wwx.imback.clientimport

import org.springframework.context.annotation.Import
import kotlin.reflect.KClass

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Import(HttpClientRegistrar::class)
annotation class HttpClientScan(vararg val value: String = [],
                                val basePackages: Array<String> = [],
                                val annotationClass: KClass<out Annotation> = HttpClient::class)