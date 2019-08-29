package info.fandroid.chat.presentation.injection

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import info.fandroid.chat.cache.AccountCacheImpl
import info.fandroid.chat.cache.ChatDatabase
import info.fandroid.chat.cache.SharedPrefsManager
import info.fandroid.chat.data.account.AccountCache
import info.fandroid.chat.data.friends.FriendsCache
import javax.inject.Singleton

@Module
class CacheModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAccountCache(prefsManager: SharedPrefsManager): AccountCache = AccountCacheImpl(prefsManager)

    @Provides
    @Singleton
    fun provideChatDatabase(context: Context): ChatDatabase {
        return ChatDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFriendsCache(chatDatabase: ChatDatabase): FriendsCache {
        return chatDatabase.friendsDao
    }
}