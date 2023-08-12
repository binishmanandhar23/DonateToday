package com.sanket.donatetoday.utils

import android.content.res.Resources
import kotlin.math.roundToInt

fun Int.dp2px() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.px2dp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.px2dpFloat(): Float = this / Resources.getSystem().displayMetrics.density
fun Float.px2dp() = (this / Resources.getSystem().displayMetrics.density).roundToInt()
fun Float.px2dpFloat() = (this / Resources.getSystem().displayMetrics.density)
fun Float.dp2px(): Float = this * Resources.getSystem().displayMetrics.density