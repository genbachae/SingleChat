package ru.genbach.chat.ui

import android.app.Application
import dagger.Component
import ru.genbach.chat.presentation.injection.AppModule
import ru.genbach.chat.presentation.injection.CacheModule
import ru.genbach.chat.presentation.injection.RemoteModule
import ru.genbach.chat.presentation.injection.ViewModelModule
import ru.genbach.chat.ui.account.AccountActivity
import ru.genbach.chat.ui.account.AccountFragment
import ru.genbach.chat.ui.core.navigation.RouteActivity
import ru.genbach.chat.ui.firebase.FirebaseService
import ru.genbach.chat.ui.friends.FriendRequestsFragment
import ru.genbach.chat.ui.friends.FriendsFragment
import ru.genbach.chat.ui.home.HomeActivity
import ru.genbach.chat.ui.home.ChatsFragment
import ru.genbach.chat.ui.home.MessagesFragment
import ru.genbach.chat.ui.login.LoginFragment
import ru.genbach.chat.ui.register.RegisterActivity
import ru.genbach.chat.ui.register.RegisterFragment
import javax.inject.Singleton

class App : Application() { //  Класс приложения. Для инициализации компонентов приложения.

    companion object {
        lateinit var appComponent: AppComponent     //   компонент для внедрения зависимостей. Тип: AppComponent.
    }
    //  переопределенная функция жизненного цикла приложения. Вызывается метод инициализации компонента(initAppComponent()). Ничего не принимает. Ничего не возвращает.
    override fun onCreate() {
        super.onCreate()

        initAppComponent()
    }

    private fun initAppComponent() {    //  инициализирует DaggerAppComponent. Ничего не принимает. Ничего не возвращает.
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
    }
}

@Singleton
@Component(modules = [AppModule::class, CacheModule::class, RemoteModule::class, ViewModelModule::class])
//  Для определения, в какие классы будут внедрятся зависимости(RegisterActivity, RegisterFragment, FirebaseService).
//  Использует модули для получения зависимостей: AppModule, CacheModule, RemoteModule, ViewModelModule.
interface AppComponent {    //  добавлен в 9м уроке.

    //activities
    fun inject(activity: RegisterActivity)
    fun inject(activity: RouteActivity)             //  добавлен в 4м уроке.
    fun inject(activity: HomeActivity)              //  добавлен в 4м уроке.
    fun inject(activity: AccountActivity)           //  добавлен в 6м уроке.

    //fragments
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: LoginFragment)              //  добавлен в 4м уроке.
    fun inject(fragment: ChatsFragment)              //  добавлен в 4м уроке.
    fun inject(fragment: FriendsFragment)           //  добавлен в 5м уроке.
    fun inject(fragment: FriendRequestsFragment)    //  добавлен в 5м уроке.
    fun inject(fragment: AccountFragment)           //  добавлен в 6м уроке.
    fun inject(fragment: MessagesFragment)

    //services
    fun inject(service: FirebaseService)
}