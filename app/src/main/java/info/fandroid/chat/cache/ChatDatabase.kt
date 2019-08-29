package info.fandroid.chat.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import info.fandroid.chat.cache.friends.FriendsDao
import info.fandroid.chat.domain.friends.FriendEntity

@Database(entities = [FriendEntity::class], version = 2, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract val friendsDao: FriendsDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getInstance(context: Context): ChatDatabase {

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