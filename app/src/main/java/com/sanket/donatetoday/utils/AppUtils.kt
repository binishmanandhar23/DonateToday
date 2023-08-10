package com.sanket.donatetoday.utils


const val MaximumMonthlyGoal = 10000

fun String?.emptyIfNull() = this ?: ""

fun String?.getInitial(): String? = this?.getOrNull(0)?.toString()