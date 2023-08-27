package com.sanket.donatetoday.modules.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.sanket.donatetoday.modules.common.DonateTodayTabPager
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.data.TabItem


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageScreen() {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = UniversalHorizontalPaddingInDp)){
        stickyHeader { 
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = UniversalVerticalPaddingInDp)) {
                Text(text = "Chats", style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}