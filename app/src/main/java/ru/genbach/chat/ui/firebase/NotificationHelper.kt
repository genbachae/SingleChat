package ru.genbach.chat.ui.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import ru.genbach.chat.R
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.messages.ContactEntity
import ru.genbach.chat.domain.messages.GetMessagesWithContact
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.remote.service.ApiService
import ru.genbach.chat.ui.home.HomeActivity
import ru.genbach.chat.ui.home.MessagesActivity
import org.json.JSONObject
import javax.inject.Inject

class NotificationHelper @Inject constructor(   // Вспомогательный класс. Для парса firebase-сообщений и отображения нотификаций.
    val context: Context, val getMessagesWithContact: GetMessagesWithContact) : ContextWrapper(context) {   //  объект

    companion object {
        const val MESSAGE = "message"
        const val JSON_MESSAGE = "firebase_json_message"
        const val TYPE = "type"
        const val TYPE_ADD_FRIEND = "addFriend"
        const val TYPE_APPROVED_FRIEND = "approveFriendRequest"
        const val TYPE_CANCELLED_FRIEND_REQUEST = "cancelFriendRequest"
        const val TYPE_SEND_MESSAGE = "sendMessage"

        const val notificationId = 110                                      //  идентификатор нотификации.
    }
    //  объект для управления нотификациями. Тип: NotificationManager.
    var mManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannels()
    }


    private fun createChannels() {      //  создает канал нотификации. Необходим для новых версий Android.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            val androidChannel = NotificationChannel(
                context.packageName,
                "${context.packageName}.notification_chanel", NotificationManager.IMPORTANCE_DEFAULT
            )
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.GREEN
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            mManager.createNotificationChannel(androidChannel)
        }
    }

    //  предварительный парс сообщения. Исходя из типа (valtype), делегирует отображение нотификации одному из методов.
    //  Принимает Ничего не возвращает.
    fun sendNotification(remoteMessage: RemoteMessage?) {   //  добавлен case для входящего сообщения.
        if (remoteMessage?.data == null) {
            return
        }

        val message = remoteMessage.data[MESSAGE]
        val jsonMessage = JSONObject(message).getJSONObject(JSON_MESSAGE)

        val type = jsonMessage.getString(TYPE)
        when (type) {
            TYPE_ADD_FRIEND -> sendAddFriendNotification(jsonMessage)
            TYPE_APPROVED_FRIEND -> sendApprovedFriendNotification(jsonMessage)
            TYPE_CANCELLED_FRIEND_REQUEST -> sendCancelledFriendNotification(jsonMessage)
            TYPE_SEND_MESSAGE -> sendMessageNotification(jsonMessage)
        }
    }
    //  добавлено создание нотификации. Передает интент запуска HomeActivity.
    //  делегируют парс сообщения функции parseFriend(…), показывает тост о приглашении дружбы,
    //  принятии или отмене приглашения дружбы. Принимают Ничего не возвращают.
    private fun sendAddFriendNotification(jsonMessage: JSONObject) {
        val friend = parseFriend(jsonMessage)

        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra("type", TYPE_ADD_FRIEND)

        createNotification(
            getString(R.string.friend_request),
            "${friend.name} ${context.getString(R.string.wants_add_as_friend)}",
            intent
        )
    }

    //  добавлено создание нотификации. Передает интент запуска HomeActivity.
    //  делегируют парс сообщения функции parseFriend(…), показывает тост о приглашении дружбы,
    //  принятии или отмене приглашения дружбы. Принимают Ничего не возвращают.
    private fun sendApprovedFriendNotification(jsonMessage: JSONObject) {
        val friend = parseFriend(jsonMessage)

        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra("type", TYPE_APPROVED_FRIEND)

        createNotification(
            getString(R.string.friend_request_approved),
            "${friend.name} ${context.getString(R.string.approved_friend_request)}",
            intent
        )
    }
    //  добавлено создание нотификации. Передает интент запуска HomeActivity.
    //  делегируют парс сообщения функции parseFriend(…), показывает тост о приглашении дружбы,
    //  принятии или отмене приглашения дружбы. Принимают Ничего не возвращают.
    //  парсит FriendEntity из json объекта. Принимает Возвращает FriendEntity.
    private fun sendCancelledFriendNotification(jsonMessage: JSONObject) {
        val friend = parseFriend(jsonMessage)

        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra("type", TYPE_CANCELLED_FRIEND_REQUEST)

        createNotification(
            getString(R.string.friend_request_cancelled),
            "${friend.name} ${context.getString(R.string.cancelled_friend_request)}",
            intent
        )
    }
    //  делегирует парс сообщения функции parseMessage(…), показывает тост о входящем сообщении.
    //  Вызывает получение списка сообщений, что обновляет бд. Принимает JSONObject. Ничего не возвращают.
    private fun sendMessageNotification(jsonMessage: JSONObject) {
        val message = parseMessage(jsonMessage)
        //  usecase для получения списка сообщений. Тип: GetMessageseWithContact.
        getMessagesWithContact(GetMessagesWithContact.Params(message.senderId, true))

        val intent = Intent(context, MessagesActivity::class.java)

        intent.putExtra(ApiService.PARAM_CONTACT_ID, message.contact?.id)
        intent.putExtra(ApiService.PARAM_NAME, message.contact?.name)
        intent.putExtra("type", TYPE_SEND_MESSAGE)

        createNotification(
            "${message.contact?.name} ${context.getString(R.string.send_message)}",
            message.message,
            intent
        )
    }


    private fun parseFriend(jsonMessage: JSONObject): FriendEntity {

        val requestUser = if (jsonMessage.has(ApiService.PARAM_REQUEST_USER)) {
            jsonMessage.getJSONObject(ApiService.PARAM_REQUEST_USER)
        } else {
            jsonMessage.getJSONObject(ApiService.PARAM_APPROVED_USER)
        }

        val friendsId = jsonMessage.getLong(ApiService.PARAM_FRIENDS_ID)

        val id = requestUser.getLong(ApiService.PARAM_USER_ID)
        val name = requestUser.getString(ApiService.PARAM_NAME)
        val email = requestUser.getString(ApiService.PARAM_EMAIL)
        val status = requestUser.getString(ApiService.PARAM_STATUS)
        val image = requestUser.getString(ApiService.PARAM_USER_ID)

        return FriendEntity(id, name, email, friendsId, status, image)
    }
    //  парсит MessageEntity из json объекта. Принимает JSONObject. Возвращает MessageEntity.
    private fun parseMessage(jsonMessage: JSONObject): MessageEntity {
        val senderUser = jsonMessage.getJSONObject(ApiService.PARAM_SENDER_USER)
        val senderName = senderUser.getString(ApiService.PARAM_NAME)
        val senderImage = senderUser.getString(ApiService.PARAM_IMAGE)

        val id = jsonMessage.getLong(ApiService.PARAM_MESSAGE_ID)
        val senderId = jsonMessage.getLong(ApiService.PARAM_SENDER_USER_ID)
        val receiverId = jsonMessage.getLong(ApiService.PARAM_RECEIVED_USER_ID)
        val message = jsonMessage.getString(ApiService.PARAM_MESSAGE)
        val type = jsonMessage.getInt(ApiService.PARAM_MESSAGE_TYPE)

        return MessageEntity(id, senderId, receiverId, message, 0, type, ContactEntity(senderId, senderName, senderImage))
    }
    //  создает и показывает нотификацию. Присваивает ей заголовок, иконку и интент реагирования на нажатие.
    //  Принимает String:title, message, Intent. Ничего не возвращает.
    private fun createNotification(title: String, message: String, intent: Intent) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        intent.action = "notification $notificationId"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        val contentIntent = PendingIntent.getActivity(
            context, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(
            context, context.applicationContext.packageName
        )
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setContentText(message)
        mBuilder.setContentIntent(contentIntent)
        mManager.notify(notificationId, mBuilder.build())
    }
}