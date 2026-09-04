package br.com.brunocarvalhs.chat.app.data.local

import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.core.domain.model.MessageModel

internal fun MessageModel.toEntity(): ChatMessage = ChatMessage(
    id = id,
    groupId = groupId,
    text = text,
    timestamp = timestamp,
    senderName = senderName,
    senderId = senderId,
    status = status,
    reactions = reactions
)

internal fun ChatMessage.toDomain(): MessageModel = MessageModel(
    id = id,
    groupId = groupId,
    text = text,
    senderId = senderId,
    senderName = senderName,
    timestamp = timestamp,
    status = status,
    reactions = reactions
)
