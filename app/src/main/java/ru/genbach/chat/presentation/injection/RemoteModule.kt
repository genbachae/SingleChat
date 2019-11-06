package ru.genbach.chat.presentation.injection

import dagger.Module
import dagger.Provides
import ru.genbach.chat.BuildConfig
import ru.genbach.chat.data.account.AccountRemote
import ru.genbach.chat.data.friends.FriendsRemote
import ru.genbach.chat.data.messages.MessagesRemote
import ru.genbach.chat.remote.account.AccountRemoteImpl
import ru.genbach.chat.remote.core.Request
import ru.genbach.chat.remote.friends.FriendsRemoteImpl
import ru.genbach.chat.remote.messages.MessagesRemoteImpl
import ru.genbach.chat.remote.service.ApiService
import ru.genbach.chat.remote.service.ServiceFactory
import javax.inject.Singleton

@Module
class RemoteModule {    //  Класс-модуль. Для предоставления зависимостей ApiService и AccountRemote.

    @Singleton
    @Provides
    //  функция, предоставляющая Ничего не принимает. Возвращает ApiService.
    fun provideApiService(): ApiService = ServiceFactory.makeService(BuildConfig.DEBUG)

    @Singleton
    @Provides
    //  функция, предоставляющая Использует уже предоставленный ApiService и Request(@Injectconstructor внутри Request).
    //  Принимает Request и ApiService. Возвращает AccountRemote.
    fun provideAccountRemote(request: Request, apiService: ApiService): AccountRemote {
        return AccountRemoteImpl(request, apiService)
    }

    @Singleton
    @Provides
    //  предоставляет зависимость FriendsRemote. Принимает Request и ApiService. Возвращает FriendsRemote.
    fun provideFriendsRemote(request: Request, apiService: ApiService): FriendsRemote {
        return FriendsRemoteImpl(request, apiService)
    }

    @Singleton
    @Provides
    //  предоставляет зависимость Принимает Request и ApiService. Возвращает MessagesRemote.
    fun provideMessagesRemote(request: Request, apiService: ApiService): MessagesRemote {
        return MessagesRemoteImpl(request, apiService)
    }
}