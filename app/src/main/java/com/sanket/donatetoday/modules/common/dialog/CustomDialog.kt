package com.sanket.donatetoday.modules.common.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.MutableLiveData
import com.sanket.donatetoday.modules.common.dialog.enums.DialogTypes
import com.sanket.donatetoday.modules.common.DonateTodayDialogContainer

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    dialogModifier: Modifier = Modifier,
    customDialogState: CustomDialogState,
    onDismissRequest: (() -> Unit)? = null,
    dialogProperties: DialogProperties = DialogProperties(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
    dialogContent: @Composable ColumnScope.(dialogType: DialogTypes, extraString: String?) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val customDialogEnum by customDialogState.customDialogEnum.observeAsState(DialogTypes.None)
    val visible by customDialogState.visibility.observeAsState(false)
    val extraString by customDialogState.extraString.observeAsState()
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (visible)
            DonateTodayDialogContainer(
                modifier = dialogModifier,
                onDismissRequest = { onDismissRequest?.invoke()?: customDialogState.hide() },
                dialogProperties = dialogProperties,
                contentPadding = contentPadding,
                content = {
                    dialogContent(this, customDialogEnum, extraString)
                })
        content(this)

        /*AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.background.copy(
                        alpha = 0.8f
                    )
                ).let {
                    if (visible)
                        it.clickable {
                            customDialogState.hide()
                        }
                    else
                        it
                })
        }
        AnimatedContent(modifier = modifier.align(Alignment.Center),
            targetState = visible,
            transitionSpec = { fadeIn() with fadeOut() using SizeTransform(false) }) { shown ->
            if (shown)
                dialogContent(this@Box, customDialogEnum, extraString)
            else
                Spacer(modifier = modifier)
        }*/
    }
}


@Composable
fun rememberDialogState(
    initialVisibility: Boolean = false,
    customDialogEnum: DialogTypes = DialogTypes.None
) = rememberSaveable(
    saver =
    CustomDialogState.Saver
) {
    CustomDialogState(visibility = initialVisibility, customDialogEnum = customDialogEnum)
}


class CustomDialogState(visibility: Boolean, customDialogEnum: DialogTypes) {
    val visibility = MutableLiveData(visibility)
    val customDialogEnum = MutableLiveData(customDialogEnum)
    val extraString = MutableLiveData<String?>(null)
    var dismissListener: (() -> Unit)? = null

    fun show(
        dialog: DialogTypes = DialogTypes.None,
        extraString: String? = null,
        dismissListener: (() -> Unit)? = null
    ) =
        customDialogEnum.postValue(dialog).also {
            this.extraString.postValue(extraString)
            visibility.postValue(true)
            this.dismissListener = dismissListener
        }

    fun show() = visibility.postValue(true)
    fun hide() = visibility.postValue(false).also { dismissListener?.invoke() }

    companion object {
        private const val VisibleKey = "visibility"
        private const val DialogEnumKey = "dialog_enum_key"

        val Saver = mapSaver(save = {
            mapOf(
                VisibleKey to (it.visibility.value as Boolean),
                DialogEnumKey to (it.customDialogEnum.value?.type)
            )
        }, restore = {
            CustomDialogState(
                visibility = it[VisibleKey] as Boolean,
                customDialogEnum = DialogTypes.getValue(it[DialogEnumKey] as? String)
            )
        })
    }
}