package com.sanket.donatetoday.modules.donor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.DonateTodayDivider
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.DonationGoalIndicator
import com.sanket.donatetoday.modules.common.HorizontalHeaderValue
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DonorDetailScreen(user: UserDTO?, onMessage: (UserDTO) -> Unit) {
    if (user == null)
        return
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ), verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DonateTodayProfilePicture(name = user.name)
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = { onMessage(user) }) {
                    Icon(imageVector = Icons.Default.Message, contentDescription = "Message", tint = MaterialTheme.colors.secondary)
                }
            }
        }
        item { DonateTodayDivider() }
        item {
            HorizontalHeaderValue(header = "Email", value = user.emailAddress)
        }
        item {
            AnimatedVisibility(visible = !user.phoneNo.isNullOrEmpty()) {
                HorizontalHeaderValue(
                    header = "Telephone Number",
                    value = "${user.countryPhoneCode}-${user.phoneNo}"
                )
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${user.name}'s Goal Meter",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                )
                DonationGoalIndicator(
                    reached = user.reached,
                    totalGoal = user.totalGoal
                )
            }
        }
    }
}