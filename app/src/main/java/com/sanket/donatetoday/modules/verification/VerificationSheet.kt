package com.sanket.donatetoday.modules.verification

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.DonateTodayButton
import com.sanket.donatetoday.modules.common.HorizontalHeaderValue
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerificationSheet(userDTO: UserDTO, onVerify: (userDTO: UserDTO) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ), verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        stickyHeader {
            Text(
                text = "Verify your email",
                style = MaterialTheme.typography.h3.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        item {
            Text(
                text = "You'll get an official verification check after you verify you email.",
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.onBackground,
                )
            )
        }
        item {
            HorizontalHeaderValue(header = "Send verification email to:", value = userDTO.emailAddress)
        }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                DonateTodayButton(modifier = Modifier.align(Alignment.Center), text = "Verify") {
                    onVerify(userDTO)
                }
            }
        }
    }
}