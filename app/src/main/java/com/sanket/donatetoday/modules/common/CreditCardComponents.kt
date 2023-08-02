package com.sanket.donatetoday.modules.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.R
import com.sanket.donatetoday.modules.common.data.CreditCardData
import com.sanket.donatetoday.utils.DateUtils
import com.sanket.donatetoday.utils.card.CardValidator
import com.sanket.donatetoday.utils.card.enums.Cards

@Composable
private fun DonateTodayCardNumberInput(
    modifier: Modifier,
    cardNumber: String,
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
        onValueChange = onCardNumberChanged,
        label = "Enter your card number",
        leadingIcon = {
            AnimatedContent(targetState = cardType) {
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
    onCardHolderNameChanged: (String) -> Unit
) {
    DonateTodaySingleLineTextField(
        modifier = modifier,
        value = cardHolderName,
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
        mutableStateOf(if(localDate == null) null else localDate.monthValue + 1)
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(text = "Expires on", style = MaterialTheme.typography.h5)
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DonateTodaySingleLineTextField(modifier = Modifier
                .width(90.dp)
                .padding(end = 5.dp),
                value = year?.toString() ?: "",
                onValueChange = {
                    if (it.toIntOrNull() != 0 && it.length < 5)
                        year = it.toIntOrNull()
                    onDateChange(
                        DateUtils.convertYearAndMonthToMainDateFormat(
                            year = year,
                            month = month
                        ) ?: ""
                    )
                },
                label = "YYYY",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(text = "/")
            DonateTodaySingleLineTextField(modifier = Modifier
                .width(80.dp)
                .padding(start = 5.dp),
                value = month?.toString() ?: "",
                onValueChange = { monthString ->
                    if (monthString.toIntOrNull() != 0 && (monthString.toIntOrNull() != null && monthString.toInt() > 0) && (monthString.toIntOrNull() != null && monthString.toInt() < 13))
                        month = monthString.toIntOrNull()
                    onDateChange(
                        DateUtils.convertYearAndMonthToMainDateFormat(
                            year = year,
                            month = month
                        ) ?: ""
                    )
                },
                label = "MM",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun DonateTodayCardInfoFields(
    modifier: Modifier = Modifier,
    creditCardData: CreditCardData,
    onCardDataUpdate: (CreditCardData) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DonateTodayCardNumberInput(
            modifier = modifier,
            cardNumber = creditCardData.cardNo,
            onCardNumberChanged = {
                onCardDataUpdate(creditCardData.copy(cardNo = it))
            }
        )
        DonateTodayCreditCardHolderName(
            modifier = modifier,
            cardHolderName = creditCardData.cardHolderName,
            onCardHolderNameChanged = {
                onCardDataUpdate(creditCardData.copy(cardHolderName = it))
            })
        DonateTodayCreditCardExpiry(modifier = modifier, expiryDate = creditCardData.expiresOn, onDateChange = {
            onCardDataUpdate(creditCardData.copy(expiresOn = it))
        })
    }
}