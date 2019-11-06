package ru.genbach.chat.ui.core.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.genbach.chat.ui.App
import javax.inject.Inject

class RouteActivity : AppCompatActivity() {     //  Стартовая activity. Для менеджмента запуска activity при открытии приложения.

    @Inject
    lateinit var navigator: Navigator           //   объект

    override fun onCreate(savedInstanceState: Bundle?) {    //  делегирует определение activity объекту Navigator (navigator.showMain(…)).
        super.onCreate(savedInstanceState)

        App.appComponent.inject(this)

        navigator.showMain(this)
    }
}
