package com.sanket.donatetoday.modules.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.AppLogoHorizontal
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayBottomTabs
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.home.data.SettingsItem
import com.sanket.donatetoday.modules.home.enums.SettingsEnums
import com.sanket.donatetoday.modules.home.getters.DashboardGetters
import com.sanket.donatetoday.utils.MaximumMonthlyGoal
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContainer(dashboardGetters: DashboardGetters) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val pages = remember {
        listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.Settings)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            pageCount = pages.size,
            userScrollEnabled = false,
            state = pagerState
        ) { page ->
            when (page) {
                0 -> DashboardScreenContainer(dashboardGetters = dashboardGetters)
                1 -> ListScreenContainer()
                2 -> SettingsMainContainer(dashboardGetters = dashboardGetters)
                else -> Unit
            }
        }
        DonateTodayBottomTabs(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            listOfIcons = pages,
            selectedIndex = pagerState.currentPage,
            onSelected = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            })
    }
}

@Composable
fun ListScreenContainer() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { DashboardToolbar() }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreenContainer(dashboardGetters: DashboardGetters) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        stickyHeader {
            DashboardToolbar()
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = UniversalHorizontalPaddingInDp,
                        vertical = UniversalVerticalPaddingInDp
                    )
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                DashboardInformation(
                    modifier = Modifier.fillMaxWidth(),
                    userDTO = dashboardGetters.userDTO,
                    onEditMonthlyGoal = dashboardGetters.onEditMonthlyGoal
                )
                if (dashboardGetters.userDTO.userType == UserType.Donor.type)
                    UserDashboard(listOfDonationItemUserModel = dashboardGetters.listOfDonationItemUserModel, onClick = dashboardGetters.onDonationItemUserModelClick)
            }
        }
    }
}

@Composable
fun UserDashboard(listOfDonationItemUserModel: List<DonationItemUserModel>, onClick: (DonationItemUserModel) -> Unit) {
    DashboardLists(modifier = Modifier.fillMaxWidth(), title = "Recommended", items = listOfDonationItemUserModel, onClick = onClick)
}

@Composable
fun DashboardToolbar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
        ) {
            IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search button"
                )
            }
        }
        AppLogoHorizontal(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth(),
            animate = true,
            logoSize = 30.dp,
            textSize = MaterialTheme.typography.subtitle1.fontSize
        )
    }
}

@Composable
fun DashboardInformation(
    modifier: Modifier = Modifier,
    userDTO: UserDTO,
    onEditMonthlyGoal: () -> Unit
) {
    var actualProgress by remember {
        mutableStateOf(0f)
    }
    LaunchedEffect(key1 = userDTO.reached) {
        animate(
            initialValue = 0f,
            targetValue = userDTO.reached.toFloat() / userDTO.totalGoal.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
            block = { value, _ ->
                if (!value.isNaN())
                    actualProgress = value
            })
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                if (userDTO.userType == UserType.Donor.type)
                    Text(
                        text = "Hi!",
                        style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold)
                    )
                Text(
                    text = userDTO.name,
                    style = MaterialTheme.typography.h4
                )
            }
            IconButton(onClick = onEditMonthlyGoal) {
                Icon(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape
                        )
                        .padding(10.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Set goals",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        Text(text = "Monthly goal", style = MaterialTheme.typography.h5)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp),
                progress = actualProgress,
                strokeCap = StrokeCap.Round
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current: $${userDTO.totalGoal * actualProgress}",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "Total: $${userDTO.totalGoal}",
                    style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun DashboardLists(
    modifier: Modifier = Modifier,
    title: String,
    items: List<DonationItemUserModel>,
    onClick: (DonationItemUserModel) -> Unit
) {
    AnimatedVisibility(modifier = modifier, visible = items.isNotEmpty()) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
            )
            LazyRow {
                items(items) {
                    DashboardListItem(modifier = Modifier.width(100.dp), item = it, onClick = {
                        onClick(it)
                    })
                }
            }
        }
    }
}

@Composable
private fun DashboardListItem(modifier: Modifier = Modifier, item: DonationItemUserModel, onClick: () -> Unit) {
    Column(modifier = modifier.clickable(onClick = onClick), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        DonateTodayProfilePicture(
            name = item.name,
            size = 100.dp,
            backgroundColor = MaterialTheme.colors.secondary,
            shape = MaterialTheme.shapes.medium
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}


@Composable
fun SettingsMainContainer(dashboardGetters: DashboardGetters) {
    val settingsItems = remember {
        listOf(
            SettingsItem(
                settingsEnums = SettingsEnums.EditProfile,
                icon = Icons.Default.AccountCircle
            ),
            SettingsItem(settingsEnums = SettingsEnums.Logout, icon = Icons.Default.Logout),
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = UniversalVerticalPaddingInDp,
                horizontal = UniversalHorizontalPaddingInDp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DonateTodayProfilePicture(name = dashboardGetters.userDTO.name)
            Text(text = dashboardGetters.userDTO.name, style = MaterialTheme.typography.h3)
        }
        CardContainer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
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
                settingsItems.forEach {
                    SettingsListItem(settingsItem = it, onClick = dashboardGetters.onSettingsClick)
                }
            }
        }
    }
}

@Composable
fun SettingsListItem(settingsItem: SettingsItem, onClick: (SettingsEnums) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = UniversalInnerHorizontalPaddingInDp)
            .clickable(onClick = {
                onClick(settingsItem.settingsEnums)
            }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = settingsItem.settingsEnums.string,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Icon(
            imageVector = settingsItem.icon,
            contentDescription = settingsItem.settingsEnums.string
        )
    }
}