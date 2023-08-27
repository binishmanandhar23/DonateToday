package com.sanket.donatetoday.modules.chat.data

import com.sanket.donatetoday.models.dto.MessageDTO

data class ChatData(val id: String, val name: String, val messages: List<MessageDTO> = emptyList())
