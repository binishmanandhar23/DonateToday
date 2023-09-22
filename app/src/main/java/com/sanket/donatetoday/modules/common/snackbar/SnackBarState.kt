package com.sanket.donatetoday.modules.common.snackbar


import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.sanket.donatetoday.BuildConfig
import kotlinx.coroutines.delay

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    snackBarModifier: Modifier = Modifier,
    text: String,
    snackBarState: SnackBarState,
    delay: Long? = null,
    useBox: Boolean = false,
    content: @Composable () -> Unit
) {
    val visible by snackBarState.visible.observeAsState(false)
    val overridingText by snackBarState.overridingText.observeAsState(text)
    val overridingDelay by snackBarState.overridingDelay.observeAsState(delay)
    val innerText by remember(overridingText) { mutableStateOf(overridingText) }

    overridingDelay?.let {
        LaunchedEffect(key1 = visible) {
            delay(it)
            snackBarState.hide()
        }
    }

    if (useBox)
        Box(modifier = modifier) {
            MainContents(
                content = content,
                visible = visible,
                innerText = innerText,
                modifier = snackBarModifier.align(
                    Alignment.BottomCenter
                )
            )
        }
    else
        Column(modifier = modifier) {
            MainContents(
                content = content,
                visible = visible,
                innerText = innerText,
                modifier = snackBarModifier.padding(top = 10.dp)
            )
        }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainContents(
    modifier: Modifier,
    visible: Boolean,
    innerText: String,
    content: @Composable () -> Unit
) {
    content()
    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = visible,
        transitionSpec = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeIn() with slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down
            ) + fadeOut()
        },
        label = "",
    ) { show ->
        if (show)
            Snackbar(
                modifier = modifier,
                backgroundColor = MaterialTheme.colors.onBackground,
                shape = MaterialTheme.shapes.medium,
                elevation = 5.dp
            ) {
                Text(
                    text = innerText,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colors.background
                    ),
                )
            }
        else
            Spacer(modifier = modifier.fillMaxWidth())
    }
}

@Composable
fun rememberSnackBarState(initialVisibility: Boolean = false): SnackBarState =
    rememberSaveable(saver = SnackBarState.Saver) {
        SnackBarState(initialVisibility)
    }

const val SnackBarLengthShort = 1500L
const val SnackBarLengthMedium = 2200L
const val SnackBarLengthLong = 3000L

class SnackBarState(initialVisibility: Boolean) {
    val visible = MutableLiveData(initialVisibility)
    val overridingText = MutableLiveData<String>()
    val overridingDelay = MutableLiveData<Long?>(null)

    fun show(overridingText: String? = null, overridingDelay: Long? = null) {
        visible.postValue(true)
        if (overridingText != null)
            this.overridingText.postValue(overridingText)

        this.overridingDelay.postValue(overridingDelay)
    }

    fun hide() {
        visible.postValue(false)
    }

    companion object {
        /**
         * The default [Saver] implementation for [SnackBarState].
         */
        val Saver: Saver<SnackBarState, *> = Saver(
            save = { it.visible.value },
            restore = { SnackBarState(it) }
        )
    }
}