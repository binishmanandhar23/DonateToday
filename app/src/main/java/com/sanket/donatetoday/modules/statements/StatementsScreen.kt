package com.sanket.donatetoday.modules.statements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.AllDonationTypeDTO
import com.sanket.donatetoday.models.dto.StatementDTO
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.home.DashboardToolbar
import com.sanket.donatetoday.utils.DateUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatementsScreen(userDTO: UserDTO, statements: AllDonationTypeDTO, onSearchStatements: (String) -> Unit, onClick: (id: String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        stickyHeader {
            DashboardToolbar(toolbarText = "Statements", onSearch = onSearchStatements, searchEnabled = true)
        }
        items(statements.all) { statement ->
            StatementListDesign(userDTO = userDTO, statementDTO = statement, onClick = onClick)
        }
        item {
            Spacer(modifier = Modifier.size(150.dp))
        }
    }
}

@Composable
fun StatementListDesign(userDTO: UserDTO, statementDTO: StatementDTO, onClick: (id: String) -> Unit) {
    CardContainer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = UniversalHorizontalPaddingInDp, vertical = 5.dp),
        onClick = { onClick(if (userDTO.userType == UserType.Donor.type) statementDTO.organizationId else statementDTO.userId) },
        cardColor = MaterialTheme.colors.background,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = UniversalInnerHorizontalPaddingInDp,
                    vertical = UniversalInnerVerticalPaddingInDp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DonateTodayProfilePicture(
                modifier = Modifier.weight(0.2f),
                name = if (userDTO.userType == UserType.Donor.type) statementDTO.organizationName else statementDTO.userName
            )
            Row(modifier = Modifier.weight(0.8f)) {
                Column(modifier = Modifier.weight(0.7f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(
                        text = if (userDTO.userType == UserType.Donor.type) statementDTO.organizationName else statementDTO.userName,
                        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Normal)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
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
                Column(modifier = Modifier.weight(0.3f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(text = if (userDTO.userType == UserType.Donor.type) "Donated" else "Received", style = MaterialTheme.typography.subtitle2)
                    Text(
                        text = "$ ${statementDTO.amount}",
                        style = MaterialTheme.typography.h3.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )
                    )
                }
            }
        }
    }
}