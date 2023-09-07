package com.sanket.donatetoday.modules.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Message
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m2.style.m2ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.AppLogoHorizontal
import com.sanket.donatetoday.modules.common.AutoSizeText
import com.sanket.donatetoday.modules.common.CardContainer
import com.sanket.donatetoday.modules.common.DonateTodayBottomTabs
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.DonateTodayYearNavigator
import com.sanket.donatetoday.modules.common.DonationGoalIndicator
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalInnerVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.home.data.SettingsItem
import com.sanket.donatetoday.modules.home.enums.SettingsEnums
import com.sanket.donatetoday.modules.home.getters.DashboardGetters
import com.sanket.donatetoday.modules.message.MessageScreen
import com.sanket.donatetoday.modules.organization.data.OrganizationDonorCashChartData
import com.sanket.donatetoday.modules.organization.data.OrganizationDonorChartData
import com.sanket.donatetoday.modules.statements.StatementsScreen
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContainer(dashboardGetters: DashboardGetters) {
    val pages = remember {
        listOf(
            Icons.Default.Home,
            Icons.Default.List,
            Icons.Default.Message,
            Icons.Default.Settings
        )
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        pages.size
    }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            userScrollEnabled = false,
            state = pagerState
        ) { page ->
            when (page) {
                0 -> DashboardScreenContainer(dashboardGetters = dashboardGetters)
                1 -> StatementsScreen(
                    userDTO = dashboardGetters.userDTO,
                    statements = dashboardGetters.listOfAllStatements,
                    onSearchStatements = dashboardGetters.onSearchStatements,
                    onClick = dashboardGetters.onStatementClick,
                    selectedStatementTypes = dashboardGetters.selectedStatementTypes,
                    onStatementTypeSelected = dashboardGetters.onStatementTypeSelected
                )

                2 -> MessageScreen()
                3 -> SettingsMainContainer(dashboardGetters = dashboardGetters)
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreenContainer(dashboardGetters: DashboardGetters) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        stickyHeader {
            DashboardToolbar(onSearch = {

            })
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
                    UserDashboard(
                        listOfDonationItemUserModel = dashboardGetters.listOfDonationItemUserModel,
                        onClick = dashboardGetters.onDonationItemUserModelClick,
                        year = dashboardGetters.year,
                        onYearChanged = dashboardGetters.onYearChanged,
                        donorCashChartData = dashboardGetters.donorCashChartData
                    )
                else
                    OrganizationDashboard(
                        organizationDonorCashChartData = dashboardGetters.organizationCashChartData,
                        organizationDonorChartData = dashboardGetters.organizationDonorChartData,
                        year = dashboardGetters.year,
                        onYearChanged = dashboardGetters.onYearChanged
                    )
            }
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun UserDashboard(
    year: Int,
    listOfDonationItemUserModel: List<DonationItemUserModel>,
    donorCashChartData: List<OrganizationDonorCashChartData>,
    onClick: (DonationItemUserModel) -> Unit,
    onYearChanged: (year: Int) -> Unit
) {
    val cashChartEntryModel =
        entryModelOf(*donorCashChartData.map { it.amount }.toTypedArray())
    val horizontalAxisValueFormatterForCash =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            donorCashChartData[value.toInt()].localDate.format(DateTimeFormatter.ofPattern("MMM"))
        }
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DonateTodayYearNavigator(currentYear = year, onYearChanged = onYearChanged)
            Text(
                text = "Yearly Donation Chart",
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight.Bold
            )
            AnimatedContent(targetState = year, transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }, label = "Year Navigator") {
                ProvideChartStyle(
                    chartStyle = m2ChartStyle(axisLineColor = MaterialTheme.colors.secondary)
                ) {
                    Chart(
                        chart = lineChart(),
                        model = cashChartEntryModel,
                        startAxis = rememberStartAxis(),
                        bottomAxis = rememberBottomAxis(valueFormatter = horizontalAxisValueFormatterForCash),
                    )
                }
            }
        }
        DashboardLists(
            modifier = Modifier.fillMaxWidth(),
            title = "Recommended",
            items = listOfDonationItemUserModel,
            onClick = onClick
        )
    }
}


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun OrganizationDashboard(
    organizationDonorCashChartData: List<OrganizationDonorCashChartData>,
    organizationDonorChartData: List<OrganizationDonorChartData>,
    year: Int,
    onYearChanged: (year: Int) -> Unit
) {
    val cashChartEntryModel =
        entryModelOf(*organizationDonorCashChartData.map { it.amount }.toTypedArray())
    val donorChartEntryModel =
        entryModelOf(*organizationDonorChartData.map { it.amount }.toTypedArray())
    val horizontalAxisValueFormatterForCash =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            organizationDonorCashChartData.getOrNull(value.toInt())?.localDate?.format(
                DateTimeFormatter.ofPattern(
                    "MMM yyyy"
                )
            )?: ""
        }
    val horizontalAxisValueFormatterForDonor =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            organizationDonorChartData.getOrNull(value.toInt())?.donor?.split(" ")?.getOrNull(0)
                ?: "-"
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Cash Donation Chart",
            color = MaterialTheme.colors.secondary,
            fontWeight = FontWeight.Bold
        )
        AnimatedContent(targetState = year, transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> width } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }, label = "Year Navigator") {
            ProvideChartStyle(
                chartStyle = m2ChartStyle(axisLineColor = MaterialTheme.colors.secondary)
            ) {
                Chart(
                    chart = lineChart(),
                    model = cashChartEntryModel,
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(valueFormatter = horizontalAxisValueFormatterForCash),
                )
            }
        }
        DonateTodayYearNavigator(currentYear = year, onYearChanged = onYearChanged)
        Text(
            text = "Top Donor Chart",
            color = MaterialTheme.colors.secondary,
            fontWeight = FontWeight.Bold
        )
        AnimatedContent(targetState = year, transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> width } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }, label = "Year Navigator") {
            ProvideChartStyle(
                chartStyle = m2ChartStyle(axisLineColor = MaterialTheme.colors.secondary)
            ) {
                Chart(
                    chart = columnChart(),
                    model = donorChartEntryModel,
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(valueFormatter = horizontalAxisValueFormatterForDonor),
                )
            }
        }

    }
}

@Composable
fun DashboardToolbar(
    modifier: Modifier = Modifier,
    toolbarText: String? = null,
    searchEnabled: Boolean = false,
    onSearch: (String) -> Unit
) {
    var search by remember {
        mutableStateOf(false)
    }
    var searchText by remember(search) {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = searchText) {
        onSearch(searchText)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (searchEnabled)
            AnimatedContent(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth(), targetState = search, label = "Search"
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (!it) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = { search = true }) {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search button"
                            )
                        }
                        if (toolbarText != null)
                            AutoSizeText(
                                modifier = Modifier
                                    .padding(start = 50.dp, end = 10.dp)
                                    .align(Alignment.CenterStart),
                                text = toolbarText,
                                style = MaterialTheme.typography.h3.copy(
                                    color = MaterialTheme.colors.onSurface,
                                    letterSpacing = 0.14.sp,
                                    textAlign = TextAlign.Start
                                )
                            )
                    } else {
                        DonateTodaySingleLineTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                            },
                            label = "Search",
                            trailingIcon = {
                                IconButton(onClick = { search = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear button"
                                    )
                                }
                            })
                    }
                }
            }
        else
            Spacer(modifier = Modifier.weight(0.7f))
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
                    style = MaterialTheme.typography.h4,
                    fontWeight = if (userDTO.userType == UserType.Donor.type) FontWeight.Normal else FontWeight.Bold
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
        DonationGoalIndicator(reached = userDTO.reached, totalGoal = userDTO.totalGoal)
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
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
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
private fun DashboardListItem(
    modifier: Modifier = Modifier,
    item: DonationItemUserModel,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
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