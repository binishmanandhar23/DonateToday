package com.sanket.donatetoday.modules.organization

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.dto.verifyOrganization
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.CreditCardDetailsWithCVV
import com.sanket.donatetoday.modules.common.DonateTodayButton
import com.sanket.donatetoday.modules.common.DonateTodayCircularButton
import com.sanket.donatetoday.modules.common.DonateTodayDivider
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.DonateTodayToolbar
import com.sanket.donatetoday.modules.common.DonationGoalIndicator
import com.sanket.donatetoday.modules.common.HorizontalHeaderValue
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.common.enums.DonationStateEnums
import com.sanket.donatetoday.modules.organization.data.GenericDonationData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrganizationDetailScreen(
    organization: UserDTO?,
    onBack: () -> Unit,
    onDonateItem: (String) -> Unit,
    onPhone: (UserDTO) -> Unit,
    onEmail: (UserDTO) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DonateTodayToolbar(
            toolbarText = organization?.name ?: "",
            onBack = onBack
        )
        AnimatedVisibility(visible = organization != null) {
            if (organization == null)
                return@AnimatedVisibility

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
                        name = organization!!.name,
                        verified = organization.verifyOrganization()
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
                        HorizontalHeaderValue(header = "Email", value = organization!!.emailAddress, trailingIcon = Icons.Rounded.Email, onTrailingIconClick = {
                            onEmail(organization)
                        })
                        AnimatedVisibility(visible = !organization.phoneNo.isNullOrEmpty()) {
                            HorizontalHeaderValue(
                                header = "Telephone Number",
                                value = "${organization.countryPhoneCode}-${organization.phoneNo}",
                                trailingIcon = Icons.Rounded.Phone,
                                onTrailingIconClick = {
                                    onPhone(organization)
                                }
                            )
                        }
                        AnimatedVisibility(visible = !organization.location.fullAddress.isNullOrEmpty()) {
                            HorizontalHeaderValue(
                                header = "Location",
                                value = organization.location.fullAddress,
                                trailingIcon = Icons.Rounded.Phone,
                                onTrailingIconClick = {
                                    onPhone(organization)
                                }
                            )
                        }
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
}


@Composable
fun DonationItemButton(donationItem: String, onClick: () -> Unit) {
    DonateTodayCircularButton(
        onClick = onClick,
        imageVector = when (donationItem) {
            DonationItemTypes.Cash.type -> Icons.Default.MonetizationOn
            DonationItemTypes.Clothes.type -> Icons.Default.Accessibility
            DonationItemTypes.Food.type -> Icons.Default.Fastfood
            DonationItemTypes.Utensils.type -> Icons.Default.Palette
            else -> Icons.Default.DeviceUnknown
        },
        contentDescription = donationItem.capitalize(Locale.current),
    )
}

@Composable
fun CashDonationBottomSheet(
    userDTO: UserDTO,
    onDonate: (amount: Int) -> Unit,
    onGoToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Card Details",
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight.Bold
            )
        )
        if (userDTO.cardInfo != null) {
            CreditCardDetailsWithCVV(creditCardDataDTO = userDTO.cardInfo, onDonate = onDonate)
        } else {
            Text(text = "Please insert your card details if you want to donate with cash.")
            TextButton(onClick = onGoToProfile) {
                Text(
                    text = "Go to settings",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClothesDonationBottomSheet(
    userDTO: UserDTO,
    onDonate: (clothesData: List<GenericDonationData>) -> Unit,
) {
    val leftWeight = remember {
        0.55f
    }
    val middleWeight = remember {
        0.1f
    }
    val rightWeight = remember {
        0.35f
    }
    val genericDonationData = remember {
        mutableStateListOf(GenericDonationData())
    }
    var donationState by remember {
        mutableStateOf(DonationStateEnums.Initial)
    }
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().animateContentSize()
            .padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stickyHeader {
            Text(
                text = "Clothes to Donate",
                style = MaterialTheme.typography.h3.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(leftWeight),
                    text = "Item name",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                )
                Spacer(modifier = Modifier.weight(middleWeight))
                Text(
                    modifier = Modifier.weight(rightWeight),
                    text = "Quantity",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                )
            }
        }
        item {
            DonateTodayDivider()
        }
        itemsIndexed(genericDonationData, key = { index, item ->
            index
        }) { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DonateTodaySingleLineTextField(
                    modifier = Modifier.weight(leftWeight),
                    label = "Enter item name",
                    value = item.itemName,
                    onValueChange = {
                        genericDonationData[index] = item.copy(itemName = it)
                    })
                Spacer(modifier = Modifier.weight(middleWeight))
                DonateTodaySingleLineTextField(
                    modifier = Modifier.weight(rightWeight),
                    label = "Quantity",
                    value = item.amount?.toString() ?: "",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        genericDonationData[index] = item.copy(amount = it.toIntOrNull())
                    })
            }
        }
        item {
            Row(
                modifier = Modifier.clickable(role = Role.Button) {
                    genericDonationData.add(GenericDonationData())
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add data",
                    tint = MaterialTheme.colors.secondary
                )
                Text(text = "Add more")
            }
        }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(modifier = Modifier.align(Alignment.Center), targetState = donationState, label = "Donation state") { state ->
                    when (state) {
                        DonationStateEnums.Initial ->
                            DonateTodayButton(text = "Donate") {
                                donationState = DonationStateEnums.Donating
                                coroutineScope.launch {
                                    delay(2000)
                                    donationState = DonationStateEnums.Donated
                                    delay(1500)
                                    onDonate(genericDonationData)
                                }
                            }

                        DonationStateEnums.Donating -> CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colors.primary
                        )

                        DonationStateEnums.Donated -> Column(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Donated",
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                text = "Donated",
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UtensilsDonationBottomSheet(
    userDTO: UserDTO,
    onDonate: (clothesData: List<GenericDonationData>) -> Unit,
) {
    val leftWeight = remember {
        0.55f
    }
    val middleWeight = remember {
        0.1f
    }
    val rightWeight = remember {
        0.35f
    }
    val genericDonationData = remember {
        mutableStateListOf(GenericDonationData())
    }
    var donationState by remember {
        mutableStateOf(DonationStateEnums.Initial)
    }
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().animateContentSize()
            .padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stickyHeader {
            Text(
                text = "Utensils to Donate",
                style = MaterialTheme.typography.h3.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(leftWeight),
                    text = "Item name",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                )
                Spacer(modifier = Modifier.weight(middleWeight))
                Text(
                    modifier = Modifier.weight(rightWeight),
                    text = "Quantity",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                )
            }
        }
        item {
            DonateTodayDivider()
        }
        itemsIndexed(genericDonationData, key = { index, item ->
            index
        }) { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DonateTodaySingleLineTextField(
                    modifier = Modifier.weight(leftWeight),
                    label = "Enter item name",
                    value = item.itemName,
                    onValueChange = {
                        genericDonationData[index] = item.copy(itemName = it)
                    })
                Spacer(modifier = Modifier.weight(middleWeight))
                DonateTodaySingleLineTextField(
                    modifier = Modifier.weight(rightWeight),
                    label = "Quantity",
                    value = item.amount?.toString() ?: "",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        genericDonationData[index] = item.copy(amount = it.toIntOrNull())
                    })
            }
        }
        item {
            Row(
                modifier = Modifier.clickable(role = Role.Button) {
                    genericDonationData.add(GenericDonationData())
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add data",
                    tint = MaterialTheme.colors.secondary
                )
                Text(text = "Add more")
            }
        }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(modifier = Modifier.align(Alignment.Center), targetState = donationState, label = "Donation state") { state ->
                    when (state) {
                        DonationStateEnums.Initial ->
                            DonateTodayButton(text = "Donate") {
                                donationState = DonationStateEnums.Donating
                                coroutineScope.launch {
                                    delay(2000)
                                    donationState = DonationStateEnums.Donated
                                    delay(1500)
                                    onDonate(genericDonationData)
                                }
                            }

                        DonationStateEnums.Donating -> CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colors.primary
                        )

                        DonationStateEnums.Donated -> Column(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Donated",
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                text = "Donated",
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDonationBottomSheet(
    userDTO: UserDTO,
    onDonate: (clothesData: List<GenericDonationData>) -> Unit,
) {
    val leftWeight = remember {
        0.55f
    }
    val middleWeight = remember {
        0.1f
    }
    val rightWeight = remember {
        0.35f
    }
    val genericDonationData = remember {
        mutableStateListOf(GenericDonationData())
    }
    var donationState by remember {
        mutableStateOf(DonationStateEnums.Initial)
    }
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().animateContentSize()
            .padding(
                horizontal = UniversalHorizontalPaddingInDp,
                vertical = UniversalVerticalPaddingInDp
            ), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stickyHeader {
            Text(
                text = "Food to Donate",
                style = MaterialTheme.typography.h3.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(leftWeight),
                    text = "Food name",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                )
                Spacer(modifier = Modifier.weight(middleWeight))
                Text(
                    modifier = Modifier.weight(rightWeight),
                    text = "Quantity",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                )
            }
        }
        item {
            DonateTodayDivider()
        }
        itemsIndexed(genericDonationData, key = { index, item ->
            index
        }) { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DonateTodaySingleLineTextField(
                    modifier = Modifier.weight(leftWeight),
                    label = "Enter item name",
                    value = item.itemName,
                    onValueChange = {
                        genericDonationData[index] = item.copy(itemName = it)
                    })
                Spacer(modifier = Modifier.weight(middleWeight))
                DonateTodaySingleLineTextField(
                    modifier = Modifier.weight(rightWeight),
                    label = "Quantity",
                    value = item.amount?.toString() ?: "",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        genericDonationData[index] = item.copy(amount = it.toIntOrNull())
                    })
            }
        }
        item {
            Row(
                modifier = Modifier.clickable(role = Role.Button) {
                    genericDonationData.add(GenericDonationData())
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "Add data",
                    tint = MaterialTheme.colors.secondary
                )
                Text(text = "Add more")
            }
        }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(modifier = Modifier.align(Alignment.Center), targetState = donationState, label = "Donation state") { state ->
                    when (state) {
                        DonationStateEnums.Initial ->
                            DonateTodayButton(text = "Donate") {
                                donationState = DonationStateEnums.Donating
                                coroutineScope.launch {
                                    delay(2000)
                                    donationState = DonationStateEnums.Donated
                                    delay(1500)
                                    onDonate(genericDonationData)
                                }
                            }

                        DonationStateEnums.Donating -> CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colors.primary
                        )

                        DonationStateEnums.Donated -> Column(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Donated",
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                text = "Donated",
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}