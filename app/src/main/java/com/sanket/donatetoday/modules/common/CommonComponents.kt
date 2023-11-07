package com.sanket.donatetoday.modules.common

import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.sanket.donatetoday.modules.common.data.TabItem
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.ui.theme.ColorBlack
import com.sanket.donatetoday.ui.theme.ColorPrimary
import com.sanket.donatetoday.ui.theme.ColorWhite
import com.sanket.donatetoday.utils.MaximumMonthlyGoal
import com.sanket.donatetoday.utils.card.CardValidator
import com.sanket.donatetoday.utils.card.enums.Cards
import com.sanket.donatetoday.utils.getInitial
import com.togitech.ccp.component.TogiCodeDialog
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getLibCountries
import kotlinx.coroutines.launch
import java.lang.Math.PI
import java.lang.Math.atan2
import java.lang.Math.max
import java.lang.Math.min
import kotlin.math.roundToInt

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
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(
            modifier = Modifier.border(
                width = 0.5.dp,
                color = MaterialTheme.colors.secondaryVariant,
                shape = MaterialTheme.shapes.small
            ),
            content = content
        )
        ErrorRow(
            errorText = errorText,
            errorIcon = errorIcon,
            errorTextColor = errorTextColor,
            errorIconColor = errorIconColor
        )
    }

}

@Composable
fun ErrorRow(
    errorText: String?,
    errorIcon: ImageVector?,
    errorTextColor: Color = MaterialTheme.colors.error,
    errorIconColor: Color = Color.Red
) {
    AnimatedVisibility(visible = !errorText.isNullOrEmpty()) {
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
    enabled: Boolean = true,
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
            enabled = enabled,
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
fun DonateTodayMultiLineTextField(
    modifier: Modifier = Modifier,
    value: String,
    errorText: String? = null,
    errorIcon: ImageVector? = null,
    errorTextColor: Color = MaterialTheme.colors.error,
    errorIconColor: Color = Color.Red,
    onValueChange: (String) -> Unit,
    label: String,
    labelIcon: ImageVector? = null,
    enabled: Boolean = true,
    @DrawableRes labelIconResId: Int? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 3,
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
            enabled = enabled,
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
            maxLines = maxLines,
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
    leadingIcon: ImageVector? = null,
    hideText: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.animateContentSize(),
        onClick = onClick,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = shape
    ) {
        leadingIcon?.let {
            Icon(imageVector = leadingIcon, contentDescription = text)
            if (!hideText)
                Spacer(modifier = Modifier.size(10.dp))
        }
        AnimatedVisibility(!hideText) {
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
    paddingValues: PaddingValues = PaddingValues(horizontal = 5.dp),
    onBack: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(paddingValues),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(0.2f)) {
            IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back button")
            }
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
    enabled: Boolean = true,
    onPhoneNumberChange: (String) -> Unit,
    onCountryPhoneCode: (String) -> Unit
) {
    var selectedCountry by remember(countryPhoneCode) {
        mutableStateOf(
            getLibCountries.find { it.countryPhoneCode == countryPhoneCode }
                ?: getLibCountries.find { it.countryCode == "us" } ?: getLibCountries.first()
        )
    }
    val correct by remember(phoneNumber) {
        derivedStateOf {
            phoneNumber.length == 10
        }
    }
    LaunchedEffect(key1 = selectedCountry) {
        onCountryPhoneCode(selectedCountry.countryPhoneCode)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextFieldOuterBox {
            AnimatedContent(enabled, label = "") { editable ->
                if (editable)
                    TogiCodeDialog(
                        padding = 15.dp,
                        defaultSelectedCountry = selectedCountry,
                        pickedCountry = {
                            selectedCountry = it
                        })
                else
                    Text(
                        text = countryPhoneCode
                    )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        DonateTodaySingleLineTextField(
            value = phoneNumber,
            onValueChange = {
                if (it.length < 11)
                    onPhoneNumberChange(it)
            },
            label = "Telephone No.",
            enabled = enabled,
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
    errorText: String? = null,
    errorIcon: ImageVector? = null,
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
        ErrorRow(errorText = errorText, errorIcon = errorIcon)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DonateTodayKnob(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 1f,
    percentage: Float = 0f,
    onValueChange: (Float) -> Unit
) {
    var rotation by remember(percentage) {
        mutableStateOf((percentage * 360f) - (2 * limitingAngle * percentage) + limitingAngle)
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

@Composable
fun DonateTodayBottomTabs(
    modifier: Modifier = Modifier,
    listOfIcons: List<ImageVector>,
    selectedIndex: Int,
    onSelected: (index: Int) -> Unit
) {
    val size = remember {
        50f
    }
    var previousIndex by remember {
        mutableStateOf(selectedIndex)
    }
    var offsetX by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = selectedIndex) {
        animate(
            initialValue = previousIndex * size,
            targetValue = selectedIndex * size
        ) { value, _ ->
            offsetX = value
            previousIndex = selectedIndex
        }
    }

    CardContainer(modifier = modifier, elevation = 10.dp) {
        Box {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(size.dp)
                    .offset(x = offsetX.dp)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.medium
                    )
            )
            Row {
                listOfIcons.forEachIndexed { index, imageVector ->
                    val tintColor by animateColorAsState(
                        targetValue = if (index == selectedIndex) ColorWhite else ColorBlack,
                        label = ""
                    )
                    IconButton(modifier = Modifier.size(size.dp), onClick = { onSelected(index) }) {
                        Icon(imageVector = imageVector, contentDescription = "", tint = tintColor)
                    }
                }
            }
        }
    }
}


@Composable
fun DonateTodayMonthlyGoalDialog(totalGoal: Int, onGoalChanged: (Int) -> Unit) {
    val progress by remember(totalGoal) {
        derivedStateOf {
            totalGoal / MaximumMonthlyGoal.toFloat()
        }
    }
    Text(
        text = "Set your monthly goal",
        style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold)
    )
    DonateTodayKnob(modifier = Modifier.size(180.dp), percentage = progress, onValueChange = {
        onGoalChanged(
            (it * (MaximumMonthlyGoal + 50)).coerceAtMost(MaximumMonthlyGoal.toFloat()).roundToInt()
        )
    })
    Text(
        text = "$${totalGoal}",
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
fun DonateTodayDeleteDialog(onDelete: (password: String) -> Unit) {
    var password by remember {
        mutableStateOf("")
    }
    Text(
        text = "Are you sure want to delete your data?",
        style = MaterialTheme.typography.h3.copy(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )
    Text(
        text = "Selecting yes, will wipe all of your data from the database. This process is irreversible.",
        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Normal)
    )
    DonateTodaySingleLineTextField(
        value = password,
        onValueChange = {
            password = it
        },
        label = "Type your password for confirmation",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ), visualTransformation = PasswordVisualTransformation()
    )
    DonateTodayButton(text = "Delete") {
        onDelete(password)
    }
}

@Composable
fun DonateTodayProfilePicture(
    modifier: Modifier = Modifier,
    name: String?,
    size: Dp = 50.dp,
    verified: Boolean = false,
    shape: Shape = CircleShape,
    backgroundColor: Color = MaterialTheme.colors.primary
) = Box {
    Box(
        modifier = modifier
            .size(size)
            .background(color = backgroundColor, shape = shape)
    )
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = name.getInitial() ?: "",
        style = MaterialTheme.typography.h3.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onPrimary
        )
    )
    if (verified)
        Icon(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 5.dp, y = 5.dp)
                .background(color = MaterialTheme.colors.background, shape = CircleShape),
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Verified",
            tint = Color.Green
        )
}

@Composable
fun DonationGoalIndicator(reached: Int, totalGoal: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    var actualProgress by remember(totalGoal) {
        mutableFloatStateOf(0f)
    }
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val current by remember(actualProgress) {
        derivedStateOf {
            (totalGoal * actualProgress).let {
                if (it.isNaN())
                    reached.toFloat()
                else
                    it
            }
        }
    }
    LaunchedEffect(key1 = reached, key2 = totalGoal) {
        animate(
            initialValue = 0f,
            targetValue = reached.toFloat() / totalGoal.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            block = { value, _ ->
                if (!value.isNaN())
                    actualProgress = value
            })
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .scale(if (current >= totalGoal.toFloat()) scale else 1f),
            progress = actualProgress,
            strokeCap = StrokeCap.Round
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Current: $$current",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Total: $${totalGoal}",
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun DonateTodayCircularButton(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colors.primary,
    onClick: (() -> Unit)? = null,
    imageVector: ImageVector,
    contentDescription: String? = null
) {
    CardContainer(
        modifier = modifier,
        onClick = { onClick?.invoke() },
        cardColor = backgroundColor,
        shape = CircleShape,
        elevation = 8.dp
    ) {
        Icon(
            modifier = Modifier
                .size(size)
                .padding(8.dp),
            imageVector = imageVector, contentDescription = contentDescription,
            tint = ColorWhite
        )
    }
}


@Composable
fun DonateTodayYearNavigator(
    modifier: Modifier = Modifier,
    currentYear: Int,
    onYearChanged: (year: Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onYearChanged(currentYear - 1) }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Decrease year",
                tint = MaterialTheme.colors.onBackground
            )
        }
        Icon(
            modifier = Modifier.offset(y = -(3).dp),
            imageVector = Icons.Default.DateRange,
            contentDescription = "Calendar Icon"
        )
        AnimatedContent(targetState = currentYear, transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }, label = "Year Navigator") {
            Text(
                text = it.toString(),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium)
            )
        }
        IconButton(onClick = { onYearChanged(currentYear + 1) }) {
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Increase year",
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DonateTodayTabPager(
    tabs: List<TabItem>,
    indicatorColor: Color = MaterialTheme.colors.secondary,
    indicatorHeight: Dp = 4.dp,
    containerColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.onSurface,
    contentTextSize: TextUnit = 14.sp
) {
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = containerColor,
        contentColor = contentColor,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .height(indicatorHeight)
                    .background(color = indicatorColor)
            )
        }
    ) {
        tabs.forEachIndexed { index, item ->
            val textColor by animateColorAsState(
                targetValue = if (index == pagerState.currentPage) indicatorColor else contentColor,
                label = ""
            )
            Tab(
                selected = index == pagerState.currentPage,
                text = {
                    Text(
                        text = item.title,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = contentTextSize
                    )
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title, tint = textColor)
                },
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
            )
        }
    }
    HorizontalPager(state = pagerState) {
        tabs[pagerState.currentPage].screen()
    }
}

@Composable
fun HorizontalHeaderValue(
    modifier: Modifier = Modifier,
    header: String,
    value: String?,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    if (!value.isNullOrEmpty())
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(if (trailingIcon == null) 1f else 0.8f),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = header,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal)
                )
            }
            trailingIcon?.let {
                CardContainer(
                    modifier = Modifier.weight(0.2f),
                    onClick = { onTrailingIconClick?.invoke() },
                    cardColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        modifier = Modifier.padding(5.dp),
                        imageVector = trailingIcon,
                        contentDescription = header,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
}

@Composable
fun DonateTodayChip(
    modifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    textColor: Color = MaterialTheme.colors.onSecondary,
    elevation: Dp = 3.dp,
    shape: CornerBasedShape = MaterialTheme.shapes.small,
    onRemove: () -> Unit
) {
    CardContainer(
        modifier = modifier,
        cardColor = backgroundColor,
        shape = shape,
        elevation = elevation
    ) {
        Row(
            modifier = innerModifier.padding(horizontal = 8.dp, vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.8f),
                text = text,
                style = MaterialTheme.typography.subtitle1.copy(color = textColor)
            )
            IconButton(modifier = Modifier.weight(0.2f), onClick = onRemove) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = "Remove",
                    tint = textColor
                )
            }
        }
    }
}

fun LazyGridScope.fullSpan(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}