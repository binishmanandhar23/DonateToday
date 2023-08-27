package com.sanket.donatetoday.modules.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sanket.donatetoday.models.dto.MessageDTO
import com.sanket.donatetoday.modules.chat.data.ChatData
import com.sanket.donatetoday.modules.common.DonateTodayMultiLineTextField
import com.sanket.donatetoday.modules.common.DonateTodayProfilePicture
import com.sanket.donatetoday.modules.common.DonateTodaySingleLineTextField
import com.sanket.donatetoday.modules.common.UniversalHorizontalPaddingInDp
import com.sanket.donatetoday.modules.common.UniversalVerticalPaddingInDp
import com.sanket.donatetoday.modules.common.loader.rememberLoaderState
import com.sanket.donatetoday.ui.states.ChatUIState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ChatScreen(
    chatUIState: StateFlow<ChatUIState>,
    message: String,
    onMessage: (String) -> Unit,
    onSend: () -> Unit
) {
    val uiState by chatUIState.collectAsState()
    val loaderState = rememberLoaderState()
    var chatData: ChatData? by remember {
        mutableStateOf(null)
    }
    if (uiState == ChatUIState.Initial || chatData == null)
        return

    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            is ChatUIState.Loading -> loaderState.show()
            else -> loaderState.hide()
        }
        when (val state = uiState) {
            is ChatUIState.Success -> chatData = state.chatData
            else -> Unit
        }
    }

    chatData?.let { data ->
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(
                        horizontal = UniversalHorizontalPaddingInDp,
                        vertical = UniversalVerticalPaddingInDp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DonateTodayProfilePicture(name = data.name)
                Text(text = data.name, style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold))
            }

            Row(modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                DonateTodayMultiLineTextField(
                    modifier = Modifier.weight(0.8f),
                    value = message,
                    onValueChange = onMessage,
                    label = "Message"
                )
                IconButton(modifier = Modifier.weight(0.2f),onClick = { onSend() }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send button")
                }
            }
        }
    }
}