package com.wwx.imback.util

import org.springframework.core.env.Environment
import java.math.BigDecimal
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

val PROPERTY_PATTERN: Pattern = Pattern.compile("\\$\\{([\\w\\.]+)\\}")

const val YYYY_MM = "yyyyMM"

const val DD = "dd"

const val YYYYMMDD_CHINESE_CHAR = "yyyy年MM月dd日"

const val YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss"

fun String.replaceProperty(environment: Environment?): String {
    var newString = this
    val matcher: Matcher = PROPERTY_PATTERN.matcher(this)
    while (matcher.find()) {
        val replaceString = matcher.group(0)
        val propertyName = matcher.group(1)
        newString = newString.replace(replaceString, environment?.getProperty(propertyName) ?: "")
    }
    return newString
}

fun String.firstCharToLower(): String {
    return if (this.isEmpty()) this else this.substring(0, 1).toLowerCase() + this.substring(1)
}

fun String.firstCharToUpper(): String {
    return if (this.isEmpty()) this else this.substring(0, 1).toUpperCase() + this.substring(1)
}

fun String.parseDate(pattern: String = YYYY_MM): Date = SimpleDateFormat(pattern).parse(this)

fun Date.formatDate(pattern: String = YYYY_MM): String = SimpleDateFormat(pattern).format(this)

fun Date.changeFormat(pattern: String = YYYY_MM): Date = this.formatDate(pattern).parseDate(pattern)

fun Date.toTimestamp(): Timestamp = Timestamp(this.time)

val HUNDRED: BigDecimal = BigDecimal(100)
val THOUSAND = BigDecimal(1000)
val COMPUTER_UNIT = BigDecimal(1024)