package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageStatus
import br.com.brunocarvalhs.friendssecrets.domain.services.ChatService
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
        val query = database.getReference("chats").child(groupId).limitToLast(50)
        val messages = mutableMapOf<String, MessageModel>()

        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val map = snapshot.value as? Map<String, Any> ?: return
                val message = map.toMessageModel(snapshot.key ?: "")
                messages[snapshot.key ?: ""] = message
                trySend(messages.values.toList().sortedBy { it.timestamp })
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val map = snapshot.value as? Map<String, Any> ?: return
                val message = map.toMessageModel(snapshot.key ?: "")
                messages[snapshot.key ?: ""] = message
                trySend(messages.values.toList().sortedBy { it.timestamp })
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                messages.remove(snapshot.key)
                trySend(messages.values.toList().sortedBy { it.timestamp })
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "FirebaseRealtimeManager: Error observing messages")
                close(error.toException())
            }
        }

        query.addChildEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
    }

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> =
        runCatching {
            val id = message.id.ifBlank {
                database.getReference("chats").child(groupId).push().key
                    ?: java.util.UUID.randomUUID().toString()
            }

            val reference = database.getReference("chats").child(groupId).child(id)

            val messageMap = mapOf(
                "id" to id,
                "groupId" to groupId,
                "text" to message.text,
                "senderId" to message.senderId,
                "senderName" to message.senderName,
                "timestamp" to message.timestamp,
                "status" to MessageStatus.SENT.name
            )

            reference.setValue(messageMap).await()
        }

    override suspend fun clearMessages(groupId: String): Result<Unit> = runCatching {
        database.getReference("chats").child(groupId).removeValue().await()
    }

    private fun Map<String, Any>.toMessageModel(key: String): MessageModel {
        return MessageModel(
            id = key,
            groupId = this["groupId"] as? String ?: "",
            text = this["text"] as? String ?: "",
            senderId = this["senderId"] as? String ?: "",
            senderName = this["senderName"] as? String ?: "",
            timestamp = (this["timestamp"] as? Long) ?: System.currentTimeMillis(),
            status = MessageStatus.valueOf(this["status"] as? String ?: MessageStatus.SENT.name)
        )
    }
}
