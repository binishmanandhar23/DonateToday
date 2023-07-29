package com.sanket.donatetoday.modules.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalSpacingInDp

@Composable
fun RegistrationScreenMain(
    emailAddress: String,
    password: String,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignIn: () -> Unit
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                    )
                    DonateTodaySingleLineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = onPasswordChanged,
                        label = "Password",
                        labelIconResId = R.drawable.ic_password,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
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
                            onClick = onSignIn
                        )
                    }
                }
            }
        }
    }
}