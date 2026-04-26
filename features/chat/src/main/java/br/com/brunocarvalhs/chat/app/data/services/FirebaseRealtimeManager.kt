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

    override fun getMessages(groupId: String): Flow<List<MessageModel>> = callbackFlow {
        val query = database.getReference(PATH).child(groupId)
            .limitToLast(LIMIT_TO_LAST)

        val messages = mutableMapOf<String, MessageModel>()

        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val map = snapshot.value as? Map<String, Any> ?: return
                val message = map.toMessageModel(snapshot.key ?: EMPTY_STRING, groupId)
                messages[snapshot.key ?: EMPTY_STRING] = message
                trySend(messages.values.toList().sortedBy { it.timestamp })
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val map = snapshot.value as? Map<String, Any> ?: return
                val message = map.toMessageModel(snapshot.key ?: EMPTY_STRING, groupId)
                messages[snapshot.key ?: EMPTY_STRING] = message
                trySend(messages.values.toList().sortedBy { it.timestamp })
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                messages.remove(snapshot.key)
                trySend(messages.values.toList().sortedBy { it.timestamp })
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // No-op: Child moved is not relevant for this message list
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException())
                close(error.toException())
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

    private fun Map<String, Any>.toMessageModel(key: String, groupId: String): MessageModel {
        val statusOrdinal = (this[KEY_STATUS] as? Long)?.toInt() ?: MessageStatus.SENT.ordinal
        val status = MessageStatus.entries.getOrElse(statusOrdinal) { MessageStatus.SENT }

        return MessageModel(
            id = key,
            groupId = groupId,
            text = this[KEY_TEXT] as? String ?: EMPTY_STRING,
            senderId = this[KEY_SENDER_ID] as? String ?: EMPTY_STRING,
            senderName = this[KEY_SENDER_NAME] as? String ?: EMPTY_STRING,
            timestamp = (this[KEY_TIMESTAMP] as? Long) ?: System.currentTimeMillis(),
            status = status
        )
    }

    private companion object {
        private const val PATH = "chats"
        const val KEY_TEXT = "t"
        const val KEY_SENDER_ID = "si"
        const val KEY_SENDER_NAME = "sn"
        const val KEY_TIMESTAMP = "ts"
        const val KEY_STATUS = "s"
        const val LIMIT_TO_LAST = 20
        const val EMPTY_STRING = ""
    }
}
