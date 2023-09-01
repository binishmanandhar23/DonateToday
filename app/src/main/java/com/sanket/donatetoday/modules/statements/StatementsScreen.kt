package com.sanket.donatetoday.modules.statements

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.AllDonationTypeDTO
import com.sanket.donatetoday.models.dto.StatementDTO
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayCircularButton
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.home.DashboardToolbar
import com.sanket.donatetoday.utils.DateUtils

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementsScreen(
    userDTO: UserDTO,
    statements: AllDonationTypeDTO,
    selectedStatementTypes: DonationItemTypes?,
    onSearchStatements: (String) -> Unit,
    onStatementTypeSelected: (DonationItemTypes?) -> Unit,
    onClick: (id: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stickyHeader {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardToolbar(
                    toolbarText = "Statements",
                    onSearch = onSearchStatements,
                    searchEnabled = true
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    kotlin.run {
                        val chipColor by animateColorAsState(
                            targetValue = if (null == selectedStatementTypes) MaterialTheme.colors.secondary else MaterialTheme.colors.onSecondary,
                            label = "Chip color"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (null == selectedStatementTypes) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onBackground,
                            label = "Text color"
                        )
                        CardContainer(
                            cardColor = chipColor,
                            elevation = 5.dp,
                            onClick = {
                                onStatementTypeSelected(null)
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
                            targetValue = if (it == selectedStatementTypes) MaterialTheme.colors.secondary else MaterialTheme.colors.onSecondary,
                            label = "Chip color"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (it == selectedStatementTypes) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onBackground,
                            label = "Text color"
                        )
                        CardContainer(
                            cardColor = chipColor,
                            elevation = 5.dp,
                            onClick = {
                                onStatementTypeSelected(it)
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
        }

        items(when (selectedStatementTypes) {
            DonationItemTypes.Cash -> statements.cash
            DonationItemTypes.Clothes -> statements.clothes
            DonationItemTypes.Food -> statements.food
            DonationItemTypes.Utensils -> statements.utensils
            else -> statements.all
        }, key = {
            it.date
        }) { statement ->
            StatementListDesign(
                modifier = Modifier
                    .animateItemPlacement()
                    .animateContentSize(),
                userDTO = userDTO,
                statementDTO = statement,
                onClick = onClick
            )
        }
        item {
            Spacer(modifier = Modifier.size(150.dp))
        }
    }
}

@Composable
fun StatementListDesign(
    modifier: Modifier = Modifier,
    userDTO: UserDTO,
    statementDTO: StatementDTO,
    onClick: (id: String) -> Unit
) {
    CardContainer(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = UniversalHorizontalPaddingInDp, vertical = 5.dp),
        onClick = { onClick(if (userDTO.userType == UserType.Donor.type) statementDTO.organizationId else statementDTO.userId) },
        cardColor = MaterialTheme.colors.background,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = UniversalInnerHorizontalPaddingInDp,
                    vertical = UniversalInnerVerticalPaddingInDp
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DonateTodayProfilePicture(
                    modifier = Modifier.weight(0.2f),
                    name = if (userDTO.userType == UserType.Donor.type) statementDTO.organizationName else statementDTO.userName
                )
                Row(modifier = Modifier.weight(0.8f)) {
                    Column(
                        modifier = Modifier.weight(0.7f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = if (userDTO.userType == UserType.Donor.type) statementDTO.organizationName else statementDTO.userName,
                            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Normal)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date")
                            Text(
                                text = DateUtils.convertMainDateFormatTo(date = statementDTO.date),
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Italic
                                )
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(0.3f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        when (statementDTO.donationType) {
                            DonationItemTypes.Cash.type -> {
                                Text(
                                    text = if (userDTO.userType == UserType.Donor.type) "Donated" else "Received",
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Text(
                                    text = "$ ${statementDTO.amount}",
                                    style = MaterialTheme.typography.h3.copy(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.End
                                    )
                                )
                            }

                            DonationItemTypes.Clothes.type -> DonateTodayCircularButton(
                                imageVector = Icons.Default.Accessibility,
                                contentDescription = statementDTO.donationType
                            )

                            else -> Unit
                        }
                    }
                }
            }
            when (statementDTO.donationType) {
                DonationItemTypes.Clothes.type -> {
                    val itemSubList = remember(statementDTO.clothesDonationData) {
                        (statementDTO.clothesDonationData ?: emptyList()).chunked(3)
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemSubList.forEach { innerList ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                innerList.forEach {
                                    Box(modifier = Modifier.weight(0.33f)) {
                                        CardContainer(
                                            cardColor = MaterialTheme.colors.secondary,
                                            elevation = 0.dp
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 7.dp
                                                ),
                                                text = "${it.amount} ${it.itemName}",
                                                style = MaterialTheme.typography.h4.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.End,
                                                    color = MaterialTheme.colors.onSecondary
                                                )
                                            )
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
}
