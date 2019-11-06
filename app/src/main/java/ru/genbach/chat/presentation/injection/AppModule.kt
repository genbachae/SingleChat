package ru.genbach.chat.presentation.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.genbach.chat.data.account.AccountCache
import ru.genbach.chat.data.account.AccountRemote
import ru.genbach.chat.data.account.AccountRepositoryImpl
import ru.genbach.chat.data.friends.FriendsCache
import ru.genbach.chat.data.friends.FriendsRemote
import ru.genbach.chat.data.friends.FriendsRepositoryImpl
import ru.genbach.chat.data.media.MediaRepositoryImpl
import ru.genbach.chat.data.messages.MessagesCache
import ru.genbach.chat.data.messages.MessagesRemote
import ru.genbach.chat.data.messages.MessagesRepositoryImpl
import ru.genbach.chat.domain.account.AccountRepository
import ru.genbach.chat.domain.friends.FriendsRepository
import ru.genbach.chat.domain.media.MediaRepository
import ru.genbach.chat.domain.messages.MessagesRepository
import javax.inject.Singleton

@Module
/*  Пример:
Для выполнения регистрации, классу Register (use case) необходим объект репозитория (AccountRepository).
AppModule предоставляет ему эту зависимость.*/
class AppModule(private val context: Context) { //  Класс-модуль. Для предоставления зависимостей контекста и репозитория.
            //  context – контекст приложения. Тип: Context.
    @Provides
    @Singleton
    //  функция, предоставляющая контекст. Ничего не принимает. Возвращает Context.
    fun provideAppContext(): Context = context

    @Provides
    @Singleton
    //  функция, предоставляющая Использует уже предоставленные в других модулях зависимости AccountRemote(remote) и
    //  AccountCache(cache). Принимает AccountRemote и AccountCache. Возвращает AccountRepository.
    fun provideAccountRepository(remote: AccountRemote, cache: AccountCache): AccountRepository {
        return AccountRepositoryImpl(remote, cache)
    }

    @Provides
    @Singleton
    // предоставляет зависимость Принимает FriendsRemote и AccountCache. Возвращает FriendsRepository.
    //  добавлен принимаемый параметр
    fun provideFriendsRepository(remote: FriendsRemote, accountCache: AccountCache, friendsCache: FriendsCache): FriendsRepository {
        return FriendsRepositoryImpl(accountCache, remote, friendsCache)
    }

    @Provides
    @Singleton
    // предоставляет зависимость Принимает Context. Возвращает MediaRepository.
    fun provideMediaRepository(context: Context): MediaRepository {
        return MediaRepositoryImpl(context)
    }

    @Provides
    @Singleton
    //  предоставляет зависимость Принимает MessageRemote, MessageCache, AccountCache. Возвращает MessagesRepository.
    fun provideMessagesRepository(remote: MessagesRemote, cache: MessagesCache, accountCache: AccountCache): MessagesRepository {
        return MessagesRepositoryImpl(remote, cache, accountCache)
    }
}