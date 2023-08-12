package com.sanket.donatetoday.modules.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.R
import com.sanket.donatetoday.models.dto.CreditCardDataDTO
import com.sanket.donatetoday.utils.DateUtils
import com.sanket.donatetoday.utils.card.CardValidator
import com.sanket.donatetoday.utils.card.enums.Cards
import com.sanket.donatetoday.utils.px2dp

@Composable
private fun DonateTodayCardNumberInput(
    modifier: Modifier,
    cardNumber: String,
    enabled: Boolean = true,
    onCardNumberChanged: (String) -> Unit
) {
    val cardType by remember(cardNumber) {
        derivedStateOf {
            CardValidator().getCardType(cardNumber)
        }
    }
    DonateTodaySingleLineTextField(
        modifier = modifier,
        value = cardNumber,
        enabled = enabled,
        onValueChange = onCardNumberChanged,
        label = "Enter your card number",
        leadingIcon = {
            AnimatedContent(targetState = cardType, label = "") {
                Image(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    painter = painterResource(
                        id = when (it) {
                            Cards.MAESTRO -> R.drawable.ic_maestro_logo
                            Cards.MASTERCARD -> R.drawable.ic_mastercard_logo
                            Cards.DINERSCLUB -> R.drawable.ic_diners_club_logo
                            Cards.LASER -> R.drawable.ic_laser_logo
                            Cards.JCB -> R.drawable.ic_jcb_logo
                            Cards.UNIONPAY -> R.drawable.ic_unionpay_logo
                            Cards.DISCOVER -> R.drawable.ic_discover_card_logo
                            Cards.AMEX -> R.drawable.ic_american_express_logo
                            Cards.VISA -> R.drawable.ic_visa_logo
                            else -> R.drawable.ic_unknown_credit_card
                        }
                    ),
                    contentDescription = "Card Type"
                )
            }
        }
    )
}

@Composable
private fun DonateTodayCreditCardHolderName(
    modifier: Modifier = Modifier,
    cardHolderName: String,
    enabled: Boolean = true,
    onCardHolderNameChanged: (String) -> Unit
) {
    DonateTodaySingleLineTextField(
        modifier = modifier,
        value = cardHolderName,
        enabled = enabled,
        onValueChange = onCardHolderNameChanged,
        label = "Enter Cardholder's Name",
        leadingIcon = {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Enter Cardholder's Name")
        }
    )
}

@Composable
private fun DonateTodayCreditCardExpiry(
    modifier: Modifier = Modifier,
    expiryDate: String,
    onDateChange: (expiresOn: String) -> Unit
) {
    val localDate = remember(expiryDate) {
        DateUtils.convertMainDateFormatToLocalDate(expiryDate)
    }
    var year by remember(localDate) {
        mutableStateOf(localDate?.year)
    }
    var month by remember(localDate) {
        mutableStateOf(localDate?.monthValue)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = "Expires on", style = MaterialTheme.typography.h5)
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DonateTodaySingleLineTextField(
                modifier = Modifier
                    .width(90.dp)
                    .padding(end = 5.dp),
                value = year?.toString() ?: "",
                onValueChange = {
                    if (it.toIntOrNull() == null)
                        year = null
                    else if (it.toInt() in 1..9999) {
                        year = it.toIntOrNull()
                        onDateChange(
                            DateUtils.convertYearAndMonthToMainDateFormat(
                                year = year,
                                month = month
                            ) ?: ""
                        )
                    }
                },
                label = "YYYY",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            Text(text = "/")
            DonateTodaySingleLineTextField(
                modifier = Modifier
                    .width(80.dp)
                    .padding(start = 5.dp),
                value = month?.toString() ?: "",
                onValueChange = { monthString ->
                    if (monthString.toIntOrNull() == null)
                        month = null
                    else if (monthString.toInt() != 0 && monthString.toInt() > 0 && monthString.toInt() < 13) {
                        month = monthString.toIntOrNull()
                        onDateChange(
                            DateUtils.convertYearAndMonthToMainDateFormat(
                                year = year,
                                month = month
                            ) ?: ""
                        )
                    }
                },
                label = "MM",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}

@Composable
fun DonateTodayCardInfoFields(
    modifier: Modifier = Modifier,
    creditCardDataDTO: CreditCardDataDTO,
    onCardDataUpdate: (CreditCardDataDTO) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DonateTodayCardNumberInput(
            modifier = modifier,
            cardNumber = creditCardDataDTO.cardNo,
            onCardNumberChanged = {
                onCardDataUpdate(creditCardDataDTO.copy(cardNo = it))
            }
        )
        DonateTodayCreditCardHolderName(
            modifier = modifier,
            cardHolderName = creditCardDataDTO.cardHolderName,
            onCardHolderNameChanged = {
                onCardDataUpdate(creditCardDataDTO.copy(cardHolderName = it))
            })
        DonateTodayCreditCardExpiry(
            modifier = modifier,
            expiryDate = creditCardDataDTO.expiresOn,
            onDateChange = {
                onCardDataUpdate(creditCardDataDTO.copy(expiresOn = it))
            })
    }
}

@Composable
fun CreditCardDetailsWithCVV(
    creditCardDataDTO: CreditCardDataDTO,
    onDonate: (amount: Int) -> Unit
) {
    var cvv by remember {
        mutableStateOf("")
    }
    var donationAmount by remember {
        mutableStateOf("")
    }
    var errorIcon: ImageVector? by remember {
        mutableStateOf(null)
    }
    var errorText: String? by remember {
        mutableStateOf(null)
    }
    var amountErrorIcon: ImageVector? by remember {
        mutableStateOf(null)
    }
    var amountErrorText: String? by remember {
        mutableStateOf(null)
    }
    var rowSize by remember {
        mutableStateOf(IntSize.Zero)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DonateTodayCardNumberInput(
            modifier = Modifier.fillMaxWidth(),
            cardNumber = creditCardDataDTO.cardNo,
            enabled = false,
            onCardNumberChanged = {}
        )
        DonateTodayCreditCardHolderName(
            modifier = Modifier.fillMaxWidth(),
            cardHolderName = creditCardDataDTO.cardHolderName,
            enabled = false,
            onCardHolderNameChanged = {})
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(bottom = 10.dp).onSizeChanged {
                    rowSize = it
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DonateTodaySingleLineTextField(
                modifier = Modifier.width((rowSize.width * 0.25f).px2dp().dp),
                value = cvv, onValueChange = {
                    if (it.length <= 3)
                        cvv = it
                }, label = "CVV",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                errorIcon = errorIcon,
                errorText = errorText
            )
            Spacer(modifier = Modifier.width((rowSize.width * 0.05f).px2dp().dp),)
            DonateTodaySingleLineTextField(
                modifier = Modifier.width((rowSize.width *  0.7f).px2dp().dp),
                value = donationAmount, onValueChange = {
                    donationAmount = it
                }, label = "Enter donation amount", keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                errorIcon = amountErrorIcon,
                errorText = amountErrorText
            )
        }
        DonateTodayButton(text = "Donate") {
            if (cvv.isEmpty() || cvv.length < 3) {
                errorIcon = Icons.Default.Error
                errorText = "Incorrect CVV"
            } else if (donationAmount.isEmpty() || donationAmount.toIntOrNull() == 0) {
                amountErrorIcon = Icons.Default.Error
                amountErrorText = "Invalid amount"
            } else
                onDonate(donationAmount.toInt())
        }
    }
}