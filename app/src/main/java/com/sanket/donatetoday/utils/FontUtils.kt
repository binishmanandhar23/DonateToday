package com.sanket.donatetoday.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.sanket.donatetoday.R

object FontUtils {
    val TTCommonFamily = FontFamily(
        Font(R.font.tt_common_regular,  weight = FontWeight.Normal),
        Font(R.font.tt_common_italic,  weight = FontWeight.Normal, style = FontStyle.Italic),
        Font(R.font.tt_common_bold,  weight = FontWeight.Bold)
    )
}