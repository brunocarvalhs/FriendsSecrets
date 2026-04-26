package br.com.brunocarvalhs.chat.app.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import br.com.brunocarvalhs.chat.app.`data`.model.ChatMessage
import br.com.brunocarvalhs.friendssecrets.domain.model.MessageModel
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.IllegalArgumentException
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ChatMessageDao_Impl(
  __db: RoomDatabase,
) : ChatMessageDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfChatMessage: EntityInsertAdapter<ChatMessage>
  init {
    this.__db = __db
    this.__insertAdapterOfChatMessage = object : EntityInsertAdapter<ChatMessage>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `chat_messages` (`id`,`groupId`,`text`,`isFromMe`,`timestamp`,`senderName`,`senderId`,`status`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ChatMessage) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.groupId)
        statement.bindText(3, entity.text)
        val _tmp: Int = if (entity.isFromMe) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        statement.bindLong(5, entity.timestamp)
        statement.bindText(6, entity.senderName)
        statement.bindText(7, entity.senderId)
        statement.bindText(8, __MessageStatus_enumToString(entity.status))
      }
    }
  }

  public override suspend fun insertMessages(messages: List<ChatMessage>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfChatMessage.insert(_connection, messages)
  }

  public override suspend fun insertMessage(message: ChatMessage): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfChatMessage.insert(_connection, message)
  }

  public override fun getMessages(groupId: String): Flow<List<ChatMessage>> {
    val _sql: String = "SELECT * FROM chat_messages WHERE groupId = ? ORDER BY timestamp ASC"
    return createFlow(__db, false, arrayOf("chat_messages")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, groupId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfGroupId: Int = getColumnIndexOrThrow(_stmt, "groupId")
        val _columnIndexOfText: Int = getColumnIndexOrThrow(_stmt, "text")
        val _columnIndexOfIsFromMe: Int = getColumnIndexOrThrow(_stmt, "isFromMe")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfSenderName: Int = getColumnIndexOrThrow(_stmt, "senderName")
        val _columnIndexOfSenderId: Int = getColumnIndexOrThrow(_stmt, "senderId")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: MutableList<ChatMessage> = mutableListOf()
        while (_stmt.step()) {
          val _item: ChatMessage
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpGroupId: String
          _tmpGroupId = _stmt.getText(_columnIndexOfGroupId)
          val _tmpText: String
          _tmpText = _stmt.getText(_columnIndexOfText)
          val _tmpIsFromMe: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFromMe).toInt()
          _tmpIsFromMe = _tmp != 0
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpSenderName: String
          _tmpSenderName = _stmt.getText(_columnIndexOfSenderName)
          val _tmpSenderId: String
          _tmpSenderId = _stmt.getText(_columnIndexOfSenderId)
          val _tmpStatus: MessageModel.MessageStatus
          _tmpStatus = __MessageStatus_stringToEnum(_stmt.getText(_columnIndexOfStatus))
          _item = ChatMessage(_tmpId,_tmpGroupId,_tmpText,_tmpIsFromMe,_tmpTimestamp,_tmpSenderName,_tmpSenderId,_tmpStatus)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearMessages(groupId: String) {
    val _sql: String = "DELETE FROM chat_messages WHERE groupId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, groupId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private fun __MessageStatus_enumToString(_value: MessageModel.MessageStatus): String = when (_value) {
    MessageModel.MessageStatus.SENDING -> "SENDING"
    MessageModel.MessageStatus.SENT -> "SENT"
    MessageModel.MessageStatus.ERROR -> "ERROR"
  }

  private fun __MessageStatus_stringToEnum(_value: String): MessageModel.MessageStatus = when (_value) {
    "SENDING" -> MessageModel.MessageStatus.SENDING
    "SENT" -> MessageModel.MessageStatus.SENT
    "ERROR" -> MessageModel.MessageStatus.ERROR
    else -> throw IllegalArgumentException("Can't convert value to enum, unknown value: " + _value)
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
