package com.sanket.donatetoday.modules.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.modules.common.AppLogoHorizontal
import com.sanket.donatetoday.modules.common.DonateTodayBottomTabs
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContainer() {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val pages = remember {
        listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.AccountBox)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(pageCount = pages.size, userScrollEnabled = false, state = pagerState) { page ->
            when (page) {
                0 -> DashboardScreenContainer()
                1 -> ListScreenContainer()
                2 -> DashboardScreenContainer()
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
fun ListScreenContainer(){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        item { DashboardToolbar() }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreenContainer() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        stickyHeader {
            DashboardToolbar()
        }
    }
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