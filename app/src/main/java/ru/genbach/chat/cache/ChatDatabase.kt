package ru.genbach.chat.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.genbach.chat.cache.friends.FriendsDao
import ru.genbach.chat.cache.messages.MessagesDao
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.messages.MessageEntity     //  9 урок: В @Database добавлен MessageEntity.

@Database(entities = [FriendEntity::class, MessageEntity::class], version = 5, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {  //  Класс базы данных. Для инициализации бд. Наследуется от:    RoomDatabase.
    abstract val friendsDao: FriendsDao     //  7й объект
    abstract val messagesDao: MessagesDao   //  9 урок: объект MessagesDao.

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null      //  синглтон. Аннотация @Volatile обеспечивает актуальность объекта во всех потоках.

        fun getInstance(context: Context): ChatDatabase {   //  инициализирует базу данных. Принимает Возвращает ChatDatabase.

            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "chat_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }

            return instance
        }
    }
}