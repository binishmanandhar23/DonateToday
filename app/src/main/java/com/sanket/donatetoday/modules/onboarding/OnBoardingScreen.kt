package com.sanket.donatetoday.modules.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanket.donatetoday.R
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.User
import com.sanket.donatetoday.modules.common.AppLogo
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayButton
import com.sanket.donatetoday.modules.common.DonateTodayCardInfoFields
import com.sanket.donatetoday.modules.common.DonateTodayCheckBox
import com.sanket.donatetoday.modules.common.DonateTodayCheckBoxItems
import com.sanket.donatetoday.modules.common.DonateTodayDivider
import com.sanket.donatetoday.modules.common.DonateTodayPhoneNumberInput
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.DonateTodayToolbar
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalSpacingInDp
import com.sanket.donatetoday.modules.common.data.CreditCardData
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.common.map.DonateTodayAddPlaces
import com.sanket.donatetoday.utils.emptyIfNull

@Composable
fun LoginScreenMain(
    emailAddress: String,
    password: String,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        AppLogo(
            modifier = Modifier
                .weight(0.4f)
                .scale(0.7f)
        )
        Box(modifier = Modifier.weight(0.6f)) {
            CardContainer {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = UniversalInnerHorizontalPaddingInDp,
                            vertical = UniversalInnerVerticalPaddingInDp
                        )
                        .fillMaxWidth()
                        .padding(horizontal = UniversalInnerHorizontalPaddingInDp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(UniversalVerticalSpacingInDp)
                ) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold)
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailAddress,
                        onValueChange = onEmailAddressChanged,
                        label = "Email Address",
                        labelIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = onPasswordChanged,
                        label = "Password",
                        labelIconResId = R.drawable.ic_password,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            onSignIn()
                        }),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    DonateTodayButton(text = "Sign In", onClick = onSignIn)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Haven't created an account yet?",
                            style = MaterialTheme.typography.body2
                        )
                        DonateTodayButton(
                            modifier = Modifier.padding(start = 10.dp),
                            text = "Sign Up",
                            backgroundColor = MaterialTheme.colors.secondary,
                            fontSize = 12.sp,
                            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 2.dp),
                            onClick = onSignUp
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegistrationScreenMain(
    user: User,
    onBack: () -> Unit,
    onUpdate: (User) -> Unit,
    onSignUp: () -> Unit,
    onAddNewPlace: () -> Unit
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    var confirmPassword by remember(user) {
        mutableStateOf("")
    }
    var addCardInfo by remember {
        mutableStateOf(false)
    }
    val rotateCardInfo by animateFloatAsState(targetValue = if (addCardInfo) 180f else 0f,
        label = ""
    )
    val errorText by remember(user.password) {
        derivedStateOf {
            if (user.password.isNotEmpty() && confirmPassword.isNotEmpty() && user.password != confirmPassword)
                "Passwords don't match"
            else if (user.password.isNotEmpty() && confirmPassword.isNotEmpty() && user.password == confirmPassword)
                "Passwords match"
            else
                null
        }
    }
    val errorIcon by remember(user.password) {
        derivedStateOf {
            if (user.password.isNotEmpty() && confirmPassword.isNotEmpty() && user.password != confirmPassword)
                Icons.Default.Close
            else if (user.password.isNotEmpty() && confirmPassword.isNotEmpty() && user.password == confirmPassword)
                Icons.Default.Check
            else
                null
        }
    }
    val errorColor by animateColorAsState(
        targetValue = if (user.password.isNotEmpty() && confirmPassword.isNotEmpty() && user.password == confirmPassword)
            Color.Green
        else
            Color.Red, label = ""
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        stickyHeader {
            DonateTodayToolbar(
                toolbarText = if (user.userType == UserType.Donor.type) "Sign up as Donor" else "Sign up as Organization",
                onBack = onBack
            )
        }
        item {
            CardContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = UniversalHorizontalPaddingInDp,
                        vertical = UniversalVerticalPaddingInDp
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = UniversalInnerHorizontalPaddingInDp,
                            vertical = UniversalInnerVerticalPaddingInDp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        UniversalVerticalSpacingInDp
                    )
                ) {
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = user.emailAddress,
                        onValueChange = {
                            onUpdate(user.copy(emailAddress = it))
                        },
                        label = "E-mail Address",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = user.name,
                        onValueChange = {
                            onUpdate(user.copy(name = it))
                        },
                        label = if (user.userType == UserType.Donor.type) "Name" else "Organization's Name",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = user.password,
                        onValueChange = {
                            onUpdate(user.copy(password = it))
                        },
                        label = "Password",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                AnimatedContent(targetState = showPassword, label = "") {
                                    Icon(
                                        painter = painterResource(id = if (it) R.drawable.ic_hide_password else R.drawable.ic_show_password),
                                        contentDescription = if (it) "Hide Password" else "Show Password",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                        },
                        label = "Confirm Password",
                        errorIcon = errorIcon,
                        errorText = errorText,
                        errorIconColor = errorColor, errorTextColor = errorColor,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                AnimatedContent(targetState = showPassword, label = "") {
                                    Icon(
                                        painter = painterResource(id = if (it) R.drawable.ic_hide_password else R.drawable.ic_show_password),
                                        contentDescription = if (it) "Hide Password" else "Show Password",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }
                    )
                    DonateTodayPhoneNumberInput(
                        modifier = Modifier.fillMaxWidth(),
                        countryPhoneCode = user.countryPhoneCode.emptyIfNull(),
                        phoneNumber = user.phoneNo.emptyIfNull(),
                        onPhoneNumberChange = {
                            onUpdate(user.copy(phoneNo = it))
                        },
                        onCountryPhoneCode = {
                            onUpdate(user.copy(countryPhoneCode = it))
                        })
                    DonateTodayCheckBoxItems(
                        modifier = Modifier.fillMaxWidth(),
                        header = if (user.userType == UserType.Donor.type) "Items you'll likely donate:" else "Items you'll receive for donation:",
                        items = DonationItemTypes.values().map { it.type },
                        selectedItems = user.donationItemTypes,
                        onCheckedChanged = { index, item, checked ->
                            val items = user.donationItemTypes.toMutableList()
                            if(checked)
                                items.add(item.lowercase())
                            else
                                items.remove(item.lowercase())
                            onUpdate(user.copy(donationItemTypes = items))
                        }
                    )
                    DonateTodayDivider()
                    DonateTodayCheckBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { addCardInfo = !addCardInfo },
                        text = "Card Details (Optional)",
                        textColor = MaterialTheme.colors.secondary,
                        fontWeight = FontWeight.Bold,
                        isChecked = addCardInfo,
                        onCheckedChanged = {
                            addCardInfo = it
                        }, trailingIcon = {
                            Icon(modifier = Modifier.rotate(rotateCardInfo), imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        })
                    AnimatedVisibility(visible = addCardInfo) {
                        DonateTodayCardInfoFields(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = UniversalInnerHorizontalPaddingInDp),
                            creditCardData = user.cardInfo?: CreditCardData(),
                            onCardDataUpdate = {
                                onUpdate(user.copy(cardInfo = it))
                            })
                    }
                    DonateTodayDivider()
                    DonateTodayAddPlaces(modifier = Modifier.fillMaxWidth(), onAddNewPlace = onAddNewPlace)
                    DonateTodayDivider()
                    DonateTodayButton(text = "Sign up", onClick = onSignUp)
                }
            }
        }
    }
}

@Composable
fun SignUpOptionDialog(asDonor: () -> Unit, asOrganization: () -> Unit) {
    Text(
        text = "Sign up as:",
        style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold)
    )
    DonateTodayButton(text = "Donor", onClick = asDonor)
    DonateTodayButton(text = "Organization", onClick = asOrganization)
}