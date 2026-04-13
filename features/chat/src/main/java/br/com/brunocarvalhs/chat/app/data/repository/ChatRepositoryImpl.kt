package br.com.brunocarvalhs.chat.app.data.repository

import br.com.brunocarvalhs.chat.app.data.local.ChatMessageDao
import br.com.brunocarvalhs.chat.app.data.model.ChatMessage
import br.com.brunocarvalhs.chat.app.domain.repository.ChatRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    override fun getMessages(groupId: String): Flow<List<ChatMessage>> {
        observeAndSyncMessages(groupId)
        return chatMessageDao.getMessages(groupId)
    }

    private fun observeAndSyncMessages(groupId: String) {
        val reference = database.getReference("chats").child(groupId)
        
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessage::class.java)
                message?.let {
                    repositoryScope.launch {
                        chatMessageDao.insertMessage(it.copy(id = snapshot.key ?: it.id, groupId = groupId))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Timber.e(error.toException(), "Error observing messages for group $groupId")
            }
        }
        
        reference.addChildEventListener(listener)
        // Note: In a real app, you might want to manage the lifecycle of this listener
    }

    override suspend fun syncMessages(groupId: String) {
        try {
            val snapshot = database.getReference("chats").child(groupId).get().await()
            val messages = mutableListOf<ChatMessage>()
            snapshot.children.forEach { child ->
                child.getValue(ChatMessage::class.java)?.let {
                    messages.add(it.copy(id = child.key ?: it.id, groupId = groupId))
                }
            }
            if (messages.isNotEmpty()) {
                chatMessageDao.insertMessages(messages)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error syncing messages for group $groupId")
        }
    }

    override suspend fun sendMessage(groupId: String, message: ChatMessage): Result<Unit> = runCatching {
        val reference = database.getReference("chats").child(groupId).push()
        val messageWithId = message.copy(id = reference.key ?: message.id, groupId = groupId)
        reference.setValue(messageWithId).await()
        // We don't strictly need to insert here because the ChildEventListener will pick it up,
        // but doing it here makes the UI snappier if it's listening to the DB.
        chatMessageDao.insertMessage(messageWithId)
    }
}
