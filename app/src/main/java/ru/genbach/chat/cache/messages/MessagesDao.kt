package ru.genbach.chat.cache.messages

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.genbach.chat.data.messages.MessagesCache
import ru.genbach.chat.domain.messages.MessageEntity

@Dao
interface MessagesDao : MessagesCache {     //  Интерфейс Dao. Для формирования SQL запросов.

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: MessageEntity): Long

    @Update
    fun update(entity: MessageEntity)

    @Transaction
    //  сохраняет сообщение в бд. Добавляет новое значение MessageEntity в таблицу (insert(…)).
    //  При наличии в таблице MessageEntity с таким же ключом — обновляет его (update(…)).
    //  Принимает MessageEntity. Ничего не возвращает.
    override fun saveMessage(entity: MessageEntity) {
        if (insert(entity) == -1L) {
            update(entity)
        }
    }
    //  формирует запрос для получения чатов из бд. Ничего не принимает. Возвращает List<MessageEntity>.
    @Query("SELECT * from messages_table ORDER BY message_date DESC")
    override fun getChats(): List<MessageEntity>

    //  формирует запрос для получения чатов из бд. Использование LiveData дает возможность прослушивать изменение данных в бд.
    //  Это даст нам возможность обновлять список сообщений чата в реальном времени.
    //  Функция ничего не принимает. Возвращает LiveData<List<MessageEntity>>.
    @Query("SELECT * from messages_table ORDER BY message_date DESC")
    fun getLiveChats(): LiveData<List<MessageEntity>>

    //  формирует запрос для получения сообщений из бд по идентификатору контакта. Принимает Long: contactId. Возвращает List<MessageEntity>.
    @Query("SELECT * from messages_table WHERE sender_id = :contactId OR receiver_id = :contactId ORDER BY message_date ASC")
    override fun getMessagesWithContact(contactId: Long): List<MessageEntity>

    //  формирует запрос для получения сообщений из бд по идентификатору контакта. Принимает Long: contactId. Возвращает LiveData<List<MessageEntity>>.
    @Query("SELECT * from messages_table WHERE sender_id = :contactId OR receiver_id = :contactId ORDER BY message_date ASC")
    fun getLiveMessagesWithContact(contactId: Long): LiveData<List<MessageEntity>>
}