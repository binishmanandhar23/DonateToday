package com.sanket.donatetoday.modules.common.loader


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.sanket.donatetoday.ui.theme.ColorWhite
import kotlinx.coroutines.delay

@Composable
fun DonateTodayLoader(
    modifier: Modifier = Modifier,
    loaderState: LoaderState,
    content: @Composable () -> Unit
) {
    val visible by loaderState.visible.observeAsState(false)

    Box(modifier = modifier) {
        MainContents(
            content = content,
            visible = visible,
        )
    }

}

@Composable
private fun MainContents(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    content()
    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = visible,
        transitionSpec = {
            fadeIn() with fadeOut()
        },
        label = "",
    ) { show ->
        if (show)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = ColorWhite.copy(alpha = 0.4f))
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.secondary,
                    strokeCap = StrokeCap.Butt
                )
            }
    }
}

@Composable
fun rememberLoaderState(initialVisibility: Boolean = false): LoaderState =
    rememberSaveable(saver = LoaderState.Saver) {
        LoaderState(initialVisibility)
    }


class LoaderState(initialVisibility: Boolean) {
    val visible = MutableLiveData(initialVisibility)

    fun show() {
        visible.postValue(true)
    }

    fun hide() {
        visible.postValue(false)
    }

    companion object {
        /**
         * The default [Saver] implementation for [LoaderState].
         */
        val Saver: Saver<LoaderState, *> = Saver(
            save = { it.visible.value },
            restore = { LoaderState(it) }
        )
    }
}