package com.wwx.imback.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val YYYY_MM = "yyyyMM"
    const val DD = "dd"
    const val YYYYMMDD_CHINESE_CHAR = "yyyy年MM月dd日"
    const val YYYYMMDD_HHMMSS = "yyyy/MM/dd HH:mm:ss"
    const val YYYYMMDD_HHMMSS2 = "yyyy-MM-dd HH:mm:ss"

    fun formatDate(pattern: String, date: Date): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    fun parseDate(pattern: String, strDate: String): Date {
        val format = SimpleDateFormat(pattern)
        return format.parse(strDate)
    }
}