package com.sanket.donatetoday.modules.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.KeyboardType
import com.sanket.donatetoday.modules.common.AppLogo
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalSpacingInDp

@Composable
fun RegistrationScreenMain() {
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
        AppLogo(modifier = Modifier.scale(0.7f))
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
                    value = "",
                    onValueChange = {},
                    label = "Email Address",
                    labelIcon = Icons.Default.Email
                )
                DonateTodaySingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    label = "Password",
                    labelIcon = Icons.Default.KeyboardArrowDown,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
        }
    }
}