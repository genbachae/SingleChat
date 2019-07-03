package info.fandroid.chat.ui

import android.app.Application
import dagger.Component
import info.fandroid.chat.ui.fragment.RegisterFragment
import info.fandroid.chat.presentation.injection.AppModule
import info.fandroid.chat.presentation.injection.CacheModule
import info.fandroid.chat.presentation.injection.RemoteModule
import info.fandroid.chat.presentation.injection.ViewModelModule
import info.fandroid.chat.ui.activity.RegisterActivity
import info.fandroid.chat.ui.service.FirebaseService
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

    //fragments
    fun inject(fragment: RegisterFragment)

    //services
    fun inject(service: FirebaseService)

}