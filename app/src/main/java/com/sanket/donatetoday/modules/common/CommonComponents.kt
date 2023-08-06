package com.sanket.donatetoday.modules.common

import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sanket.donatetoday.R
import com.sanket.donatetoday.ui.theme.ColorBlack
import com.sanket.donatetoday.ui.theme.ColorPrimary
import com.sanket.donatetoday.utils.card.CardValidator
import com.sanket.donatetoday.utils.card.enums.Cards
import com.togitech.ccp.component.TogiCodeDialog
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getLibCountries
import java.lang.Math.PI
import java.lang.Math.atan2

val UniversalHorizontalPaddingInDp = 22.dp
val UniversalVerticalPaddingInDp = 18.dp
val UniversalVerticalSpacingInDp = 15.dp
val UniversalInnerHorizontalPaddingInDp = 12.dp
val UniversalInnerVerticalPaddingInDp = 22.dp

@Composable
fun AppLogo(modifier: Modifier = Modifier, animate: Boolean = false) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ), label = ""
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
fun AppLogoHorizontal(
    modifier: Modifier = Modifier,
    logoSize: Dp? = null,
    animate: Boolean = false,
    textSize: TextUnit = MaterialTheme.typography.h2.fontSize
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.scale(if (animate) scale else 1f).let {
                if (logoSize != null)
                    it.size(logoSize)
                else
                    it
            },
            painter = painterResource(id = R.drawable.ic_donate_today),
            contentDescription = stringResource(
                id = R.string.app_name
            )
        )
        AutoSizeText(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h2.copy(
                fontSize = textSize,
                fontWeight = FontWeight.Bold
            )
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
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colors.error,
    errorIconColor: Color = Color.Red,
    content: @Composable BoxScope.() -> Unit
) {
    TextFieldOuterBox(
        modifier = modifier,
        errorText = errorText,
        errorIcon = errorIcon,
        errorIconColor = errorIconColor,
        errorTextColor = errorTextColor
    ) {
        content(this)
    }
}

@Composable
fun TextFieldOuterBox(
    modifier: Modifier = Modifier,
    errorText: String? = null,
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colors.error,
    errorIconColor: Color = Color.Red,
    content: @Composable BoxScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(
            modifier = modifier.border(
                width = 0.5.dp,
                color = MaterialTheme.colors.secondaryVariant,
                shape = MaterialTheme.shapes.small
            ),
            content = content
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (errorIcon != null)
                Icon(imageVector = errorIcon, contentDescription = errorText, tint = errorIconColor)
            if (!errorText.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = errorText,
                    style = MaterialTheme.typography.body2.copy(
                        color = errorTextColor,
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }

}

@Composable
fun DonateTodaySingleLineTextField(
    modifier: Modifier = Modifier,
    value: String,
    errorText: String? = null,
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colors.error,
    errorIconColor: Color = Color.Red,
    onValueChange: (String) -> Unit,
    label: String,
    labelIcon: ImageVector? = null,
    @DrawableRes labelIconResId: Int? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    CoreTextFieldWithBorders(
        modifier = modifier,
        errorText = errorText,
        errorIcon = errorIcon,
        errorTextColor = errorTextColor,
        errorIconColor = errorIconColor
    ) {
        CoreCustomTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            label = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (labelIcon != null)
                        Icon(imageVector = labelIcon, contentDescription = label)
                    if (labelIconResId != null)
                        Icon(
                            painter = painterResource(id = labelIconResId),
                            contentDescription = label
                        )
                    Text(text = label)
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            maxLines = 1,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon
        )
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

@Composable
fun DonateTodayDialogContainer(
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit,
    contentPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest, properties = dialogProperties) {
        CardContainer(modifier = modifier) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = contentPadding)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = onDismissRequest
                ) {
                    Image(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close button"
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 46.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content(this)
                }
            }
        }
    }
}

@Composable
fun AutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.body1
) {
    var resizableTextSize by remember(style) {
        mutableStateOf(style)
    }
    val defaultFontSize = remember {
        16.sp
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    Text(modifier = modifier.drawWithContent {
        if (shouldDraw)
            drawContent()
    }, text = text, style = resizableTextSize, onTextLayout = {
        if (it.didOverflowWidth) {
            resizableTextSize =
                resizableTextSize.copy(fontSize = (if (resizableTextSize.fontSize.isUnspecified) defaultFontSize else resizableTextSize.fontSize) * 0.95f)
        } else
            shouldDraw = true
    }, softWrap = false)
}

@Composable
fun DonateTodayToolbar(
    modifier: Modifier = Modifier,
    toolbarText: String? = null,
    onBack: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(modifier = Modifier.weight(0.2f), onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
        }
        if (toolbarText != null)
            AutoSizeText(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 10.dp),
                text = toolbarText,
                style = MaterialTheme.typography.h3.copy(
                    color = MaterialTheme.colors.onSurface,
                    letterSpacing = 0.14.sp,
                    textAlign = TextAlign.Start
                )
            )
        AppLogoHorizontal(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth(),
            animate = true,
            logoSize = 30.dp,
            textSize = MaterialTheme.typography.subtitle1.fontSize
        )
    }
}

@Composable
fun DonateTodayPhoneNumberInput(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    countryPhoneCode: String,
    onPhoneNumberChange: (String) -> Unit,
    onCountryPhoneCode: (String) -> Unit
) {
    val selectedCountry =
        getLibCountries.find { it.countryPhoneCode == countryPhoneCode }
            ?: getLibCountries.find { it.countryCode == "us" } ?: getLibCountries.first()
    val correct by remember(phoneNumber) {
        derivedStateOf {
            phoneNumber.length == 10
        }
    }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextFieldOuterBox {
            TogiCodeDialog(
                padding = 15.dp,
                defaultSelectedCountry = selectedCountry,
                pickedCountry = {
                    onCountryPhoneCode(it.countryPhoneCode)
                })
        }
        Spacer(modifier = Modifier.size(10.dp))
        DonateTodaySingleLineTextField(
            value = phoneNumber,
            onValueChange = {
                if (it.length < 11)
                    onPhoneNumberChange(it)
            },
            label = "Telephone No.",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                AnimatedVisibility(visible = phoneNumber.isNotEmpty()) {
                    AnimatedContent(targetState = correct, label = "") {
                        Icon(
                            imageVector = if (it) Icons.Default.Check else Icons.Default.Warning,
                            contentDescription = if (it) "Correct" else "Incorrect",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun DonateTodayCheckBox(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colors.onBackground,
    fontWeight: FontWeight = FontWeight.Normal,
    isChecked: Boolean = false,
    trailingIcon: (@Composable RowScope.() -> Unit)? = null,
    onCheckedChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(checked = isChecked, onCheckedChange = onCheckedChanged)
            Text(text = text, color = textColor, fontWeight = fontWeight)
        }
        trailingIcon?.invoke(this)
    }
}

@Composable
fun DonateTodayDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.secondaryVariant,
    thickness: Dp = 1.dp
) = Divider(modifier = modifier, color = color, thickness = thickness)

@Composable
fun DonateTodayCheckBoxItems(
    modifier: Modifier = Modifier,
    header: String,
    items: List<String>,
    selectedItems: List<String>,
    onCheckedChanged: (index: Int, item: String, checked: Boolean) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = header,
            style = MaterialTheme.typography.h5.copy(
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight.Bold
            )
        )
        items.forEachIndexed { index, s ->
            DonateTodayCheckBox(
                text = s.capitalize(Locale.current),
                isChecked = selectedItems.contains(s),
                onCheckedChanged = {
                    onCheckedChanged(index, s, it)
                })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DonateTodayKnob(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 1f,
    onValueChange: (Float) -> Unit
) {
    var rotation by remember {
        mutableStateOf(limitingAngle)
    }
    var touchX by remember {
        mutableStateOf(0f)
    }
    var touchY by remember {
        mutableStateOf(0f)
    }
    var centerX by remember {
        mutableStateOf(0f)
    }
    var centerY by remember {
        mutableStateOf(0f)
    }

    CardContainer(modifier = Modifier
        .onGloballyPositioned {
            val windowBounds = it.boundsInWindow()
            centerX = windowBounds.size.width / 2f
            centerY = windowBounds.size.height / 2f
        }
        .pointerInteropFilter { event ->
            touchX = event.x
            touchY = event.y
            val angle = -kotlin.math.atan2(
                (centerX - touchX).toDouble(),
                (centerY - touchY).toDouble()
            ) * (180f / PI).toFloat()

            when (event.action) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> {
                    if (angle !in -limitingAngle..limitingAngle) {
                        val fixedAngle = if (angle in -180f..-limitingAngle) {
                            360f + angle
                        } else {
                            angle
                        }
                        rotation = fixedAngle.toFloat()

                        val percent =
                            (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                        onValueChange(percent.toFloat())
                        true
                    } else false
                }

                else -> false
            }
        }
        .rotate(rotation), shape = CircleShape) {
        Box {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color = MaterialTheme.colors.primary, shape = CircleShape)
                    .align(Alignment.TopCenter)
                    .padding(top = 5.dp)
            )
            Box(
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(10.dp)
                    .background(color = MaterialTheme.colors.secondaryVariant, shape = CircleShape)

            )
        }
    }
}



