package com.sanket.donatetoday.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sanket.donatetoday.utils.FontUtils

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 25.sp,
    ),
    h2 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 22.sp
    ),
    h4 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 20.sp
    ),
    h5 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 18.sp
    ),
    body1 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 13.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontSize = 12.sp
    ),
    button = TextStyle(
        fontFamily = FontUtils.TTCommonFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
)