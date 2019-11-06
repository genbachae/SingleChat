package ru.genbach.chat.remote.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Injectable class which returns information about the network connection state.
 */
@Singleton
//  Вспомогательный класс. Содержит: val isConnnected(проверка состояния сети), extension val Context.networkInfo(получение информации о сети).
//  Для проверки состояния сети.

//Пример: Хорошим тоном будет проверять подключение к сети, прежде чем выполнять сетевые запросы. NetworkHandler обеспечивает эту проверку.
class NetworkHandler @Inject constructor(private val context: Context) {
    val isConnected get() = context.networkInfo?.isConnected    //  проверяет подключение к сети. Если подключено вернет true, если нет – false.
}
    //  extension свойство, которое предоставляет информацию о сети используя контекст.
val Context.networkInfo: NetworkInfo? get() =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
