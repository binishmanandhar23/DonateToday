package com.sanket.donatetoday.modules.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.CreditCardDataDTO
import com.sanket.donatetoday.models.dto.LocationDTO
import com.sanket.donatetoday.modules.common.DonateTodayButton
import com.sanket.donatetoday.modules.common.DonateTodayCardInfoFields
import com.sanket.donatetoday.modules.common.DonateTodayCheckBoxItems
import com.sanket.donatetoday.modules.common.DonateTodayCircularButton
import com.sanket.donatetoday.modules.common.DonateTodayDivider
import com.sanket.donatetoday.modules.common.DonateTodayPhoneNumberInput
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.DonateTodayToolbar
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.common.map.DonateTodayAddPlaces
import com.sanket.donatetoday.modules.profile.getters.ProfileScreenGetters
import com.sanket.donatetoday.utils.emptyIfNull
import com.sanket.donatetoday.utils.verifyEmptyOrNull

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(profileScreenGetters: ProfileScreenGetters) {
    var numberOfErrors by remember {
        mutableIntStateOf(0)
    }
    var checkForErrors by remember {
        mutableStateOf(false)
    }
    var editable by remember {
        mutableStateOf(false)
    }
    var userDTO by remember(profileScreenGetters.userDTO) {
        mutableStateOf(profileScreenGetters.userDTO)
    }
    LaunchedEffect(key1 = userDTO) {
        checkForErrors = false
        numberOfErrors = 0
        if (userDTO.emailAddress.verifyEmptyOrNull())
            numberOfErrors += 1
        else if (userDTO.name.verifyEmptyOrNull())
            numberOfErrors += 1
        else if (userDTO.donationItemTypes.verifyEmptyOrNull())
            numberOfErrors += 1
        else if (userDTO.cardInfo.verifyEmptyOrNull(userType = userDTO.userType))
            numberOfErrors += 1
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = UniversalVerticalPaddingInDp,
                horizontal = UniversalHorizontalPaddingInDp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        stickyHeader {
            DonateTodayToolbar(
                toolbarText = if (editable) "Edit Profile" else "Profile",
                onBack = profileScreenGetters.onBackButton,
                paddingValues = PaddingValues()
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DonateTodayProfilePicture(name = userDTO.name)
                AnimatedVisibility(visible = !editable) {
                    DonateTodayCircularButton(onClick = {
                        editable = true
                    }, imageVector = Icons.Default.Edit, contentDescription = "Edit profile")
                }
            }
        }
        item {
            DonateTodaySingleLineTextField(
                modifier = Modifier.fillMaxWidth(),
                value = profileScreenGetters.userDTO.emailAddress,
                enabled = false,
                onValueChange = {
                    userDTO = userDTO.copy(emailAddress = it)
                },
                label = "E-mail Address",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                errorText = if (checkForErrors && userDTO.emailAddress.verifyEmptyOrNull()) "Email address must not be empty" else null,
                errorIcon = Icons.Default.Error,
            )
        }
        item {
            DonateTodaySingleLineTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userDTO.name,
                enabled = editable,
                onValueChange = {
                    userDTO = userDTO.copy(name = it)
                },
                label = if (userDTO.userType == UserType.Donor.type) "Name" else "Organization's Name",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                errorText = if (checkForErrors && userDTO.name.verifyEmptyOrNull()) "Name must not be empty" else null,
                errorIcon = Icons.Default.Error,
            )

        }
        item {
            /*DonateTodaySingleLineTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userDTO.password,
                onValueChange = {
                    userDTO =userDTO.copy(password = it))
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
                errorIcon = confirmPasswordErrorIcon,
                errorText = confirmPasswordErrorText,
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
            )*/
        }
        item {
            DonateTodayPhoneNumberInput(
                modifier = Modifier.fillMaxWidth(),
                countryPhoneCode = userDTO.countryPhoneCode.emptyIfNull(),
                phoneNumber = userDTO.phoneNo.emptyIfNull(),
                enabled = editable,
                onPhoneNumberChange = {
                    userDTO = userDTO.copy(phoneNo = it)
                },
                onCountryPhoneCode = {
                    userDTO = userDTO.copy(countryPhoneCode = it)
                })
        }
        item {
            DonateTodayCheckBoxItems(
                modifier = Modifier.fillMaxWidth(),
                header = if (userDTO.userType == UserType.Donor.type) "Items you'll likely donate:" else "Items you'll receive for donation:",
                items = DonationItemTypes.values().map { it.type },
                selectedItems = userDTO.donationItemTypes,
                onCheckedChanged = { index, item, checked ->
                    if (!editable)
                        return@DonateTodayCheckBoxItems

                    val items = userDTO.donationItemTypes.toMutableList()
                    if (checked)
                        items.add(item.lowercase())
                    else
                        items.remove(item.lowercase())
                    userDTO = userDTO.copy(donationItemTypes = items)
                },
                errorText = if (checkForErrors && userDTO.donationItemTypes.verifyEmptyOrNull()) "Please select at least one item" else null,
                errorIcon = Icons.Default.Error,
            )
        }
        item {
            DonateTodayDivider()
        }
        /*item {
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
        }*/
        item {
            Text(
                text = "Card Details",
                style = MaterialTheme.typography.h5.copy(
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        item {
            DonateTodayCardInfoFields(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = UniversalInnerHorizontalPaddingInDp),
                creditCardDataDTO = userDTO.cardInfo ?: CreditCardDataDTO(),
                enabled = editable,
                onCardDataUpdate = {
                    userDTO = userDTO.copy(cardInfo = it)
                },
                errorText = if (checkForErrors && userDTO.cardInfo.verifyEmptyOrNull(userType = userDTO.userType)) "Please fill all of the Card details correctly" else null,
                errorIcon = Icons.Default.Error,
            )
        }
        item {
            DonateTodayDivider()
        }
        item {
            if (userDTO.userType == UserType.Organization.type)
                if (userDTO.location.latitude == null || userDTO.location.longitude == null || userDTO.location.fullAddress == null)
                    DonateTodayAddPlaces(
                        modifier = Modifier.fillMaxWidth(),
                        onAddNewPlace = profileScreenGetters.onAddNewPlace
                    )
                else
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(
                                text = "Location",
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                            )
                            Text(
                                text = userDTO.location.fullAddress!!,
                                style = MaterialTheme.typography.body1.copy(fontStyle = FontStyle.Italic)
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        if(editable) {
                            DonateTodayCircularButton(
                                size = 30.dp,
                                imageVector = Icons.Rounded.Edit,
                                onClick = profileScreenGetters.onAddNewPlace
                            )
                            DonateTodayCircularButton(
                                size = 30.dp,
                                backgroundColor = Color.Red,
                                imageVector = Icons.Rounded.Delete,
                                onClick = {
                                    profileScreenGetters.onUpdateProfile(userDTO.copy(location = LocationDTO()))
                                }
                            )
                        }
                    }
        }
        item {
            if (userDTO.userType == UserType.Organization.type)
                DonateTodayDivider()
        }
        item {
            AnimatedVisibility(editable) {
                DonateTodayButton(text = "Update", onClick = {
                    checkForErrors = true
                    if (numberOfErrors == 0) {
                        editable = false
                        profileScreenGetters.onUpdateProfile(userDTO)
                    }
                })
            }
        }
    }
}