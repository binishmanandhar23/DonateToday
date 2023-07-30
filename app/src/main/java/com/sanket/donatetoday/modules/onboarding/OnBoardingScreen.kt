package com.sanket.donatetoday.modules.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanket.donatetoday.R
import com.sanket.donatetoday.modules.common.AppLogo
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayButton
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.DonateTodayToolbar
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalSpacingInDp
import com.sanket.donatetoday.modules.onboarding.data.RegistrationData
import com.sanket.donatetoday.modules.onboarding.enums.RegisterAs

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
    registerAs: RegisterAs,
    registrationData: RegistrationData,
    onBack: () -> Unit,
    onUpdate: (RegistrationData) -> Unit
) {
    var confirmPassword by remember(registrationData) {
        mutableStateOf("")
    }
    val errorText by remember(registrationData.password) {
        derivedStateOf {
            if (registrationData.password.isNotEmpty() && confirmPassword.isNotEmpty() && registrationData.password != confirmPassword)
                "Passwords don't match"
            else if (registrationData.password.isNotEmpty() && confirmPassword.isNotEmpty() && registrationData.password == confirmPassword)
                "Passwords match"
            else
                null
        }
    }
    val errorIcon by remember(registrationData.password) {
        derivedStateOf {
            if (registrationData.password.isNotEmpty() && confirmPassword.isNotEmpty() && registrationData.password != confirmPassword)
                Icons.Default.Close
            else if (registrationData.password.isNotEmpty() && confirmPassword.isNotEmpty() && registrationData.password == confirmPassword)
                Icons.Default.Check
            else
                null
        }
    }
    val errorColor by animateColorAsState(
        targetValue = if (registrationData.password.isNotEmpty() && confirmPassword.isNotEmpty() && registrationData.password == confirmPassword)
            Color.Green
        else
            Color.Red
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        stickyHeader {
            DonateTodayToolbar(
                toolbarText = if (registerAs == RegisterAs.Donor) "Sign up as Donor" else "Sign up as Organization",
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
                        value = registrationData.emailAddress,
                        onValueChange = {
                            onUpdate(registrationData.copy(emailAddress = it))
                        },
                        label = "E-mail Address",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = registrationData.name,
                        onValueChange = {
                            onUpdate(registrationData.copy(name = it))
                        },
                        label = if (registerAs == RegisterAs.Donor) "Name" else "Organization's Name",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = registrationData.password,
                        onValueChange = {
                            onUpdate(registrationData.copy(password = it))
                        },
                        label = "Password",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        visualTransformation = PasswordVisualTransformation()
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
                        visualTransformation = PasswordVisualTransformation()
                    )
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