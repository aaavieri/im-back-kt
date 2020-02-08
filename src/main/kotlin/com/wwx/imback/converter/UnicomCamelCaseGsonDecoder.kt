package com.wwx.imback.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.wwx.imback.util.firstCharToLower
import com.wwx.imback.util.firstCharToUpper
import feign.FeignException
import feign.Response
import feign.Util
import feign.codec.DecodeException
import feign.codec.Decoder
import java.io.IOException
import java.io.Reader
import java.lang.reflect.Type
import java.util.*

class UnicomCamelCaseGsonDecoder : Decoder {

    @Throws(IOException::class, DecodeException::class, FeignException::class)
    override fun decode(response: Response, type: Type): Any {
        if (response.status() == 404) return Util.emptyValueOf(type)
        if (response.body() == null) return Util.emptyValueOf(type)
        val reader = response.body().asReader()
        return try {
            if (type.typeName.startsWith("java.util.List<") || type.typeName.startsWith("java.util.ArrayList<")) {
                decodeList(reader, type)
            } else {
                decodeObject(reader, type)
            }
        } catch (e: JsonIOException) {
            if (e.cause != null && e.cause is IOException) {
                throw IOException::class.java.cast(e.cause)
            }
            throw e
        } finally {
            Util.ensureClosed(reader)
        }
    }

    private fun decodeObject(reader: Reader, type: Type): Any {
        val gson = Gson()
        val dataMap: Map<String, Any> = gson.fromJson<HashMap<String, Any>>(reader, HashMap::class.java)
        val camelCaseJson = gson.toJson(dataMap.mapKeys { (key: String, _: Any) -> this.getCamelKey(key) })
        return gson.fromJson(camelCaseJson, type)
    }

    private fun decodeList(reader: Reader, type: Type): Any {
        val gson = Gson()
        val dataList: List<Map<String, Any>> = gson.fromJson<ArrayList<HashMap<String, Any>>>(reader, ArrayList::class.java)
        val camelCaseJson = gson.toJson(dataList.map { it.mapKeys { (key: String, _: Any) -> this.getCamelKey(key) } })
        return gson.fromJson(camelCaseJson, type)
    }

    private fun getCamelKey(key: String): String = key.split("_").stream()
                .reduce { part1: String, part2: String -> part1.firstCharToLower() + part2.firstCharToUpper() }
                .orElse(key.firstCharToLower())
}