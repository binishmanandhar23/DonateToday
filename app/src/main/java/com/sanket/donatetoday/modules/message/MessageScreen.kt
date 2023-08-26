package com.sanket.donatetoday.modules.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sanket.donatetoday.modules.common.DonateTodayTabPager
import com.sanket.donatetoday.modules.common.data.TabItem


@Composable
fun MessageScreen() {
    val tabs = remember {
        listOf(TabItem(title = "Inbox", icon = Icons.Default.Inbox, screen = {
            InboxScreen()
        }), TabItem(title = "Sent",icon = Icons.Default.Send, screen = {
            SentScreen()
        }))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DonateTodayTabPager(tabs = tabs)
    }
}

@Composable
fun InboxScreen() {

}

@Composable
fun SentScreen() {

}