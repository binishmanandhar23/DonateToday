package com.sanket.donatetoday.modules.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.R
import com.sanket.donatetoday.ui.theme.ColorBlack
import com.sanket.donatetoday.ui.theme.ColorPrimary

val UniversalHorizontalPaddingInDp = 22.dp
val UniversalVerticalPaddingInDp = 18.dp
val UniversalVerticalSpacingInDp = 15.dp
val UniversalInnerHorizontalPaddingInDp = 12.dp
val UniversalInnerVerticalPaddingInDp = 22.dp

@Composable
fun AppLogo(modifier: Modifier = Modifier, animate: Boolean = false) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.scale(if (animate) scale else 1f),
            painter = painterResource(id = R.drawable.ic_donate_today),
            contentDescription = stringResource(
                id = R.string.app_name
            )
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun CardContainer(
    modifier: Modifier = Modifier,
    elevation: Dp = 5.dp,
    cardColor: Color = MaterialTheme.colors.background,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit
) {
    Card(modifier = modifier, elevation = elevation, shape = shape, backgroundColor = cardColor) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardContainer(
    modifier: Modifier = Modifier,
    elevation: Dp = 5.dp,
    cardColor: Color = MaterialTheme.colors.background,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = elevation,
        shape = shape,
        backgroundColor = cardColor
    ) {
        content()
    }
}

@Composable
fun CoreCustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = ColorPrimary
    ),
    textStyle: TextStyle = MaterialTheme.typography.body1.copy(
        color = ColorBlack,
        fontWeight = FontWeight.W500
    ),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        label = label,
        colors = colors,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun CoreTextFieldWithBorders(
    modifier: Modifier = Modifier,
    errorText: String? = null,
    content: @Composable BoxScope.() -> Unit
) {
    TextFieldOuterBox(modifier = modifier, errorText = errorText) {
        content(this)
    }
}

@Composable
fun TextFieldOuterBox(
    modifier: Modifier = Modifier,
    errorText: String? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Column {
        Box(
            modifier = modifier.border(
                width = 0.5.dp,
                color = MaterialTheme.colors.secondaryVariant,
                shape = MaterialTheme.shapes.small
            ),
            content = content
        )
        if (!errorText.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = errorText,
                style = MaterialTheme.typography.body2.copy(
                    color = MaterialTheme.colors.error,
                    fontWeight = FontWeight.Light
                )
            )
        }
    }

}

@Composable
fun DonateTodaySingleLineTextField(
    modifier: Modifier = Modifier,
    value: String,
    errorText: String? = null,
    onValueChange: (String) -> Unit,
    label: String,
    labelIcon: ImageVector? = null,
    @DrawableRes labelIconResId: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    CoreTextFieldWithBorders(modifier = modifier, errorText = errorText) {
        CoreCustomTextField(value = value, onValueChange = onValueChange, label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (labelIcon != null)
                    Icon(imageVector = labelIcon, contentDescription = label)
                if(labelIconResId != null)
                    Icon(painter = painterResource(id = labelIconResId), contentDescription = label)
                Text(text = label)
            }
        }, keyboardOptions = keyboardOptions, keyboardActions = keyboardActions, singleLine = true, maxLines = 1, visualTransformation = visualTransformation)
    }
}

@Composable
fun DonateTodayButton(
    modifier: Modifier = Modifier,
    text: String,
    allCaps: Boolean = false,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    fontSize: TextUnit = MaterialTheme.typography.button.fontSize,
    contentPadding: PaddingValues = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
    textColor: Color = MaterialTheme.colors.onSecondary,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = shape
    ) {
        Text(
            text = text.let {
                if (allCaps)
                    it.uppercase()
                else
                    it
            },
            style = MaterialTheme.typography.button.copy(color = textColor, fontSize = fontSize)
        )
    }
}

