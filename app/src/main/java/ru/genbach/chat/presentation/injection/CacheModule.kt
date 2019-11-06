package ru.genbach.chat.presentation.injection

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.genbach.chat.cache.AccountCacheImpl
import ru.genbach.chat.cache.ChatDatabase
import ru.genbach.chat.cache.SharedPrefsManager
import ru.genbach.chat.data.account.AccountCache
import ru.genbach.chat.data.friends.FriendsCache
import ru.genbach.chat.data.messages.MessagesCache
import javax.inject.Singleton
/* SharedPreferences — постоянное хранилище на платформе Android, используемое приложениями для хранения своих настроек, например.
Это хранилище является относительно постоянным, пользователь может зайти в настройки приложения и очистить данные приложения,
тем самым очистив все данные в хранилище. Мы будем использовать SharedPreferences для хранения токена,
чтобы сравнивать его с токеном в базе данных, флага авторизации и некоторых других параметров и настроек.*/
@Module
class CacheModule {     //  Класс-модуль. Для предоставления зависимостей SharedPreferences и AccountCache.

    @Provides
    @Singleton
    //  функция, предоставляющая Использует уже предоставленный в AppModule контекст (context). Принимает Context. Возвращает SharedPreferences.
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    //  функция, предоставляющая Использует уже предоставленный SharedPrefsManager (благодаря аннотации @Injectconstructor внутри SharedPrefsManager,
    //  можно не писать модуль для предоставления его зависимости.). Принимает SharedPrefsManager. Возвращает AccountCache.
    fun provideAccountCache(prefsManager: SharedPrefsManager): AccountCache = AccountCacheImpl(prefsManager)

    @Provides
    @Singleton
    //  предоставляет зависимость Принимает Context. Возвращает ChatDatabase.
    fun provideChatDatabase(context: Context): ChatDatabase {
        return ChatDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    //  предоставляет зависимость Принимает ChatDatabase. Возвращает FriendsCache.
    fun provideFriendsCache(chatDatabase: ChatDatabase): FriendsCache {
        return chatDatabase.friendsDao
    }

    @Provides
    @Singleton
    //  предоставляет зависимость MessagesCache. Принимает ChatDatabase. Возвращает MessagesCache.
    fun provideMessagesCache(chatDatabase: ChatDatabase): MessagesCache {
        return chatDatabase.messagesDao
    }
}