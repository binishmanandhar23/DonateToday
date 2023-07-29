package com.sanket.donatetoday.modules.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sanket.donatetoday.modules.common.AppLogo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(timer: Long = 1000L, onSplashEnded: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        delay(timer)
        onSplashEnded()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AppLogo(modifier = Modifier.align(Alignment.Center), animate = true)
    }
}