package info.fandroid.chat.cache.messages

import androidx.lifecycle.LiveData
import androidx.room.*
import info.fandroid.chat.data.messages.MessagesCache
import info.fandroid.chat.domain.messages.MessageEntity

@Dao
interface MessagesDao : MessagesCache {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: MessageEntity): Long

    @Update
    fun update(entity: MessageEntity)

    @Transaction
    override fun saveMessage(entity: MessageEntity) {
        if (insert(entity) == -1L) {
            update(entity)
        }
    }

    @Query("SELECT * from messages_table ORDER BY message_date DESC")
    override fun getChats(): List<MessageEntity>

    @Query("SELECT * from messages_table ORDER BY message_date DESC")
    fun getLiveChats(): LiveData<List<MessageEntity>>

    @Query("SELECT * from messages_table WHERE sender_id = :contactId OR receiver_id = :contactId ORDER BY message_date ASC")
    override fun getMessagesWithContact(contactId: Long): List<MessageEntity>

    @Query("SELECT * from messages_table WHERE sender_id = :contactId OR receiver_id = :contactId ORDER BY message_date ASC")
    fun getLiveMessagesWithContact(contactId: Long): LiveData<List<MessageEntity>>
}