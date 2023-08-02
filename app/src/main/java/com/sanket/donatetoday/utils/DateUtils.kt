package com.sanket.donatetoday.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

object DateUtils {
    const val MainDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val MainDateFormat = "yyyy-MM-dd"

    fun convertYearAndMonthToMainDateFormat(year: Int?, month: Int?) =
        if(year == null || month == null) null else LocalDate.of(year, month - 1, 1).format(DateTimeFormatter.ofPattern(MainDateFormat))

    fun convertMainDateFormatToLocalDate(date: String) = if(date.isEmpty()) null else LocalDate.parse(date, DateTimeFormatter.ofPattern(
        MainDateFormat))
}