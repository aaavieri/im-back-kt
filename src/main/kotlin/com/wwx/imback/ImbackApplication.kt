package com.wwx.imback

import com.wwx.imback.clientimport.HttpClientScan
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan("com.wwx.imback.dao")
@HttpClientScan("com.wwx.imback.client")
class ImbackApplication

fun main(args: Array<String>) {
	runApplication<ImbackApplication>(*args)
}
