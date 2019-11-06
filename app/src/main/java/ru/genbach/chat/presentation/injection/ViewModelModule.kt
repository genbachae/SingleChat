package ru.genbach.chat.presentation.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.genbach.chat.presentation.viewmodel.*

@Module
abstract class ViewModelModule {    // Класс-модуль. Для биндинга ViewModel-классов и их Фабрики.
    @Binds
    //  функция, которая биндит фабрику для создания Принимает ViewModelFactory. Возвращает ViewModelProvider.Factory.
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    //  функция, которая биндит Использует аннотацию ViewModelKey для обозначения ViewModel-класса.
    //  Принимает AccountViewModel. Возвращает ViewModel.
    //  биндит Принимает FriendsViewModel. Возвращает ViewModel.
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    abstract fun bindFriendsViewModel(friendsViewModel: FriendsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MediaViewModel::class)
    //  биндит MediaViewModel. Принимает MediaViewModel. Возвращает ViewModel.
    abstract fun bindMediaViewModel(mediaViewModel: MediaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessagesViewModel::class)
    //  биндит Принимает MessagesViewModel. Возвращает ViewModel.
    abstract fun bindMessagesViewModel(messagesViewModel: MessagesViewModel): ViewModel
}
