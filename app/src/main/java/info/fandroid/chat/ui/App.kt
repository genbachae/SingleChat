package info.fandroid.chat.ui

import android.app.Application
import dagger.Component
import info.fandroid.chat.presentation.injection.AppModule
import info.fandroid.chat.presentation.injection.CacheModule
import info.fandroid.chat.presentation.injection.RemoteModule
import info.fandroid.chat.presentation.injection.ViewModelModule
import info.fandroid.chat.ui.core.navigation.RouteActivity
import info.fandroid.chat.ui.firebase.FirebaseService
import info.fandroid.chat.ui.home.HomeActivity
import info.fandroid.chat.ui.home.ChatsFragment
import info.fandroid.chat.ui.login.LoginFragment
import info.fandroid.chat.ui.register.RegisterActivity
import info.fandroid.chat.ui.register.RegisterFragment
import javax.inject.Singleton

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
    }
}

@Singleton
@Component(modules = [AppModule::class, CacheModule::class, RemoteModule::class, ViewModelModule::class])
interface AppComponent {

    //activities
    fun inject(activity: RegisterActivity)

    fun inject(activity: RouteActivity)
    fun inject(activity: HomeActivity)

    //fragments
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: ChatsFragment)

    //services
    fun inject(service: FirebaseService)
}