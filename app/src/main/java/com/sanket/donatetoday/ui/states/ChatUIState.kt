package com.sanket.donatetoday.ui.states

import com.sanket.donatetoday.modules.chat.data.ChatData

sealed class ChatUIState(val message: String? = null, val chatData: ChatData? = null){
    object Initial: ChatUIState()

    object Loading: ChatUIState()

    class Success(chatData: ChatData?): ChatUIState(chatData = chatData)

    class Error(errorMessage: String? = null): ChatUIState(message = errorMessage)
}
