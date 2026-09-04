package br.com.brunocarvalhs.chat.app.data.services

import br.com.brunocarvalhs.chat.app.domain.services.ChatService
import br.com.brunocarvalhs.core.domain.model.MessageModel
import br.com.brunocarvalhs.core.domain.model.MessageModel.MessageStatus
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseRealtimeManager @Inject constructor(
    private val database: FirebaseDatabase
) : ChatService {

    override fun getMessages(groupId: String, sinceTimestamp: Long): Flow<List<MessageModel>> =
        callbackFlow {
            val reference = database.getReference(PATH).child(groupId)
            val query = if (sinceTimestamp > 0) {
                // Only pull messages newer than what is already cached locally,
                // instead of re-downloading the whole history on every open.
                reference.orderByChild(KEY_TIMESTAMP).startAt((sinceTimestamp + 1).toDouble())
            } else {
                reference.limitToLast(LIMIT_TO_LAST)
            }

            val listener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    emitMessage(snapshot)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    emitMessage(snapshot)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // No-op: chat clearing is a local, per-device action (see ClearMessagesUseCase).
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // No-op: Child moved is not relevant for this message list
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.toException())
                    close(error.toException())
                }

                private fun emitMessage(snapshot: DataSnapshot) {
                    val map = snapshot.value as? Map<String, Any> ?: return
                    trySend(listOf(map.toMessageModel(snapshot.key ?: EMPTY_STRING, groupId)))
                }
            }

            query.addChildEventListener(listener)
            awaitClose { query.removeEventListener(listener) }
        }

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> =
        runCatching {
            val id = message.id.ifBlank {
                database.getReference(PATH).child(groupId).push().key
                    ?: java.util.UUID.randomUUID().toString()
            }

            val reference = database.getReference(PATH).child(groupId).child(id)

            val messageMap = mapOf(
                KEY_TEXT to message.text,
                KEY_SENDER_ID to message.senderId,
                KEY_SENDER_NAME to message.senderName,
                KEY_TIMESTAMP to message.timestamp,
                KEY_STATUS to MessageStatus.SENT.ordinal
            )

            reference.setValue(messageMap).await()
        }

    override suspend fun clearMessages(groupId: String): Result<Unit> = runCatching {
        database.getReference(PATH).child(groupId).removeValue().await()
    }

    override suspend fun setReaction(
        groupId: String,
        messageId: String,
        deviceId: String,
        emoji: String?
    ): Result<Unit> = runCatching {
        val reference = database.getReference(PATH).child(groupId).child(messageId)
            .child(KEY_REACTIONS).child(deviceId)

        if (emoji.isNullOrBlank()) {
            reference.removeValue().await()
        } else {
            reference.setValue(emoji).await()
        }
    }

    private fun Map<String, Any>.toMessageModel(key: String, groupId: String): MessageModel {
        val statusOrdinal = (this[KEY_STATUS] as? Long)?.toInt() ?: MessageStatus.SENT.ordinal
        val status = MessageStatus.entries.getOrElse(statusOrdinal) { MessageStatus.SENT }

        @Suppress("UNCHECKED_CAST")
        val reactions = (this[KEY_REACTIONS] as? Map<String, String>) ?: emptyMap()

        return MessageModel(
            id = key,
            groupId = groupId,
            text = this[KEY_TEXT] as? String ?: EMPTY_STRING,
            senderId = this[KEY_SENDER_ID] as? String ?: EMPTY_STRING,
            senderName = this[KEY_SENDER_NAME] as? String ?: EMPTY_STRING,
            timestamp = (this[KEY_TIMESTAMP] as? Long) ?: System.currentTimeMillis(),
            status = status,
            reactions = reactions
        )
    }

    private companion object {
        private const val PATH = "chats"
        const val KEY_TEXT = "t"
        const val KEY_SENDER_ID = "si"
        const val KEY_SENDER_NAME = "sn"
        const val KEY_TIMESTAMP = "ts"
        const val KEY_STATUS = "s"
        const val KEY_REACTIONS = "r"
        const val LIMIT_TO_LAST = 200
        const val EMPTY_STRING = ""
    }
}
