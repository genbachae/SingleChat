package ru.genbach.chat.ui.firebase

import android.os.Handler
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.genbach.chat.domain.account.UpdateToken
import ru.genbach.chat.ui.App
import javax.inject.Inject
import android.os.Looper


class FirebaseService : FirebaseMessagingService() {    //  Сервис. Для получения сообщений и токена с Firebase.

    @Inject
    //  usecase, выполняющий обновление токена в локальной базе данных и сервере.
    //  Инициализируется при помощи Dagger (@Injectconstructor в UpdateToken). Тип: UpdateToken.
    lateinit var updateToken: UpdateToken

    @Inject
    lateinit var notificationHelper: NotificationHelper         //  объект
    //  переопределение метода жизненного цикла FirebaseMessagingService. Выполняется инъекция компонента. Ничего не принимает. Ничего не возвращает.
    override fun onCreate() {
        super.onCreate()
        App.appComponent.inject(this)
    }
    //  делегирование обработки и отображения нотификации объекту NotificationHelper.
    //  При помощи Handler(MainLooper).post выражение запущено в UI потоке.
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Handler(Looper.getMainLooper()).post {
            notificationHelper.sendNotification(remoteMessage)

        }
    }
    //  переопределение метода Вызывается когда Firebase генерирует новый токен.
    //  Делегирует обновление токена объекту updateToken. Принимает String. Ничего не возвращает.
    override fun onNewToken(token: String?) {
        Log.e("fb token", ": $token")
        if (token != null) {
            updateToken(UpdateToken.Params(token))
        }
    }
}
