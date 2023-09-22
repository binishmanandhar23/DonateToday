package com.sanket.donatetoday.modules.organization

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.dto.verifyOrganization
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayButton
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.common.fullSpan
import com.sanket.donatetoday.modules.home.DashboardToolbar

@Composable
fun AllOrganizationsList(
    allFilteredOrganizations: List<UserDTO>,
    selectedDonationTypes: List<DonationItemTypes>?,
    onSearchOrganizations: (String) -> Unit,
    onDonationTypeSelected: (DonationItemTypes?) -> Unit,
    onClick: (UserDTO) -> Unit,
    onPhone: (UserDTO) -> Unit,
    onEmail: (UserDTO) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(enabled = true, onBack = onBack)

    val scrollState = rememberLazyGridState()

    val scrollInProgress by remember(scrollState.isScrollInProgress) {
        derivedStateOf { scrollState.isScrollInProgress }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = UniversalHorizontalPaddingInDp),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = scrollState
        ) {
            fullSpan {
                Spacer(modifier = Modifier.size(140.dp))
            }
            items(allFilteredOrganizations) {
                AllOrganizationsListItem(
                    organization = it,
                    onClick = onClick,
                    onPhone = onPhone,
                    onEmail = onEmail
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DashboardToolbar(
                toolbarText = "Organizations",
                onSearch = onSearchOrganizations,
                searchEnabled = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                kotlin.run {
                    val chipColor by animateColorAsState(
                        targetValue = if (null == selectedDonationTypes) MaterialTheme.colors.secondary else MaterialTheme.colors.onSecondary,
                        label = "Chip color"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (null == selectedDonationTypes) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onBackground,
                        label = "Text color"
                    )
                    CardContainer(
                        cardColor = chipColor,
                        elevation = 5.dp,
                        onClick = {
                            onDonationTypeSelected(null)
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                            text = "All",
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                color = textColor
                            )
                        )
                    }
                }
                DonationItemTypes.values().forEach {
                    val chipColor by animateColorAsState(
                        targetValue = if (selectedDonationTypes?.contains(it) == true || selectedDonationTypes == null) MaterialTheme.colors.secondary else MaterialTheme.colors.onSecondary,
                        label = "Chip color"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (selectedDonationTypes?.contains(it) == true || selectedDonationTypes == null) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onBackground,
                        label = "Text color"
                    )
                    CardContainer(
                        cardColor = chipColor,
                        elevation = 5.dp,
                        onClick = {
                            onDonationTypeSelected(it)
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                            text = it.type.capitalize(Locale.current),
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                color = textColor
                            )
                        )
                    }
                }
            }
        }

        DonateTodayButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    vertical = UniversalVerticalPaddingInDp,
                    horizontal = UniversalHorizontalPaddingInDp
                ), text = "Go Back", onClick = onBack,
            hideText = scrollInProgress,
            leadingIcon = Icons.Default.ArrowBackIosNew
        )
    }
}

@Composable
private fun AllOrganizationsListItem(
    modifier: Modifier = Modifier,
    organization: UserDTO,
    onClick: (UserDTO) -> Unit,
    onPhone: (UserDTO) -> Unit,
    onEmail: (UserDTO) -> Unit,
) {
    CardContainer(modifier = modifier, onClick = { onClick(organization) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = UniversalInnerHorizontalPaddingInDp,
                    vertical = UniversalInnerVerticalPaddingInDp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            DonateTodayProfilePicture(name = organization.name, verified = organization.verifyOrganization())
            Text(
                text = organization.name,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardContainer(
                    onClick = { onPhone.invoke(organization) },
                    cardColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        modifier = Modifier.padding(5.dp),
                        imageVector = Icons.Rounded.Phone,
                        contentDescription = "Phone",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                CardContainer(
                    onClick = { onEmail.invoke(organization) },
                    cardColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        modifier = Modifier.padding(5.dp),
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Email",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}