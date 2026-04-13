package br.com.brunocarvalhs.friendssecrets.commons.network

import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import br.com.brunocarvalhs.friendssecrets.domain.services.ChatService
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRealtimeManager @Inject constructor(
    private val database: FirebaseDatabase
) : ChatService {

    override fun getMessages(groupId: String): Flow<List<MessageModel>> = callbackFlow {
        val reference = database.getReference("chats").child(groupId)
        val messages = mutableListOf<MessageModel>()

        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(MessageModel::class.java)
                message?.let {
                    val updatedMessage = it.copy(id = snapshot.key ?: it.id, groupId = groupId)
                    messages.add(updatedMessage)
                    trySend(messages.toList())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(MessageModel::class.java)
                message?.let { updated ->
                    val index = messages.indexOfFirst { it.id == snapshot.key }
                    if (index != -1) {
                        messages[index] = updated.copy(id = snapshot.key ?: updated.id, groupId = groupId)
                        trySend(messages.toList())
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val index = messages.indexOfFirst { it.id == snapshot.key }
                if (index != -1) {
                    messages.removeAt(index)
                    trySend(messages.toList())
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reference.addChildEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    override suspend fun sendMessage(groupId: String, message: MessageModel): Result<Unit> = runCatching {
        val reference = database.getReference("chats").child(groupId).push()
        val messageWithId = message.copy(id = reference.key ?: message.id, groupId = groupId)
        reference.setValue(messageWithId).await()
    }
}
