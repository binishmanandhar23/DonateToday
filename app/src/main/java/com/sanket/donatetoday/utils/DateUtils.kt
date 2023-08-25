package com.sanket.donatetoday.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateUtils {
    const val MainDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val MainDateFormat = "yyyy-MM-dd"
    const val StatementDateFormat = "dd MMM, yyyy HH:mm"

    fun convertYearAndMonthToMainDateFormat(year: Int?, month: Int?) =
        if (year == null || month == null) null else LocalDate.of(year, month, 1)
            .format(DateTimeFormatter.ofPattern(MainDateFormat))

    fun convertMainDateFormatToLocalDate(date: String) =
        if (date.isEmpty()) null else LocalDate.parse(
            date, DateTimeFormatter.ofPattern(
                MainDateFormat
            )
        )

    fun convertMainDateTimeFormatToLocalDate(date: String) =
        if (date.isEmpty()) null else LocalDate.parse(
            date, DateTimeFormatter.ofPattern(
                MainDateTimeFormat
            )
        )

    fun getCurrentDateTime(): String = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern(
            MainDateTimeFormat
        )
    )

    fun convertMainDateFormatTo(
        date: String,
        currentFormat: String = MainDateTimeFormat,
        format: String = StatementDateFormat
    ): String = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(currentFormat))
        .format(DateTimeFormatter.ofPattern(format))
}