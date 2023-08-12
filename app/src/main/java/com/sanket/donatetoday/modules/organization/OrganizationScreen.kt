package com.sanket.donatetoday.modules.organization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.CreditCardDetailsWithCVV
import com.sanket.donatetoday.modules.common.DonateTodayDivider
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.DonateTodayToolbar
import com.sanket.donatetoday.modules.common.DonationGoalIndicator
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.ui.theme.ColorWhite

@Composable
fun OrganizationDetailScreen(
    organization: UserDTO,
    onBack: () -> Unit,
    onDonateItem: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DonateTodayToolbar(
            toolbarText = organization.name,
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = UniversalVerticalPaddingInDp,
                    horizontal = UniversalHorizontalPaddingInDp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DonateTodayProfilePicture(
                    name = organization.name,
                    verified = organization.verified
                )
                Text(text = organization.name, style = MaterialTheme.typography.h3)
            }
            CardContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(bottom = 50.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = UniversalInnerHorizontalPaddingInDp,
                            vertical = UniversalInnerVerticalPaddingInDp
                        )
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    HorizontalHeaderValue(header = "Email", value = organization.emailAddress)
                    HorizontalHeaderValue(
                        header = "Telephone Number",
                        value = "+${organization.countryPhoneCode}-${organization.phoneNo}"
                    )
                    DonateTodayDivider()
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "${organization.name}'s Goal Meter",
                            style = MaterialTheme.typography.h3.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.secondary
                            )
                        )
                        DonationGoalIndicator(
                            reached = organization.reached,
                            totalGoal = organization.totalGoal
                        )
                    }
                    DonateTodayDivider()
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Help with the donation",
                            style = MaterialTheme.typography.h3.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.secondary
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            organization.donationItemTypes.forEach {
                                DonationItemButton(donationItem = it) {
                                    onDonateItem(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalHeaderValue(modifier: Modifier = Modifier, header: String, value: String?) {
    if (!value.isNullOrEmpty())
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
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
}

@Composable
fun DonationItemButton(donationItem: String, onClick: () -> Unit) {
    CardContainer(
        onClick = onClick,
        cardColor = MaterialTheme.colors.primary,
        shape = CircleShape,
        elevation = 8.dp
    ) {
        Icon(
            modifier = Modifier.padding(10.dp),
            imageVector = when (donationItem) {
                DonationItemTypes.Cash.type -> Icons.Default.MonetizationOn
                DonationItemTypes.Clothes.type -> Icons.Default.Accessibility
                DonationItemTypes.Food.type -> Icons.Default.Fastfood
                DonationItemTypes.Utensils.type -> Icons.Default.Palette
                else -> Icons.Default.DeviceUnknown
            }, contentDescription = donationItem.capitalize(Locale.current),
            tint = ColorWhite
        )
    }
}

@Composable
fun DonationBottomSheet(userDTO: UserDTO, onDonate: (amount: Int) -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(
            horizontal = UniversalHorizontalPaddingInDp,
            vertical = UniversalVerticalPaddingInDp
        ), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "Card Details", style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.secondary, fontWeight = FontWeight.Bold))
        if (userDTO.cardInfo != null) {
            CreditCardDetailsWithCVV(creditCardDataDTO = userDTO.cardInfo, onDonate = onDonate)
        } else {
            Text(text = "Please insert your card details if you want to donate with cash.")
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Go to settings",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}