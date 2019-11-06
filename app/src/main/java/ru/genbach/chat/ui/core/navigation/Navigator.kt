package ru.genbach.chat.ui.core.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ru.genbach.chat.R
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.presentation.Authenticator
import ru.genbach.chat.presentation.viewmodel.MediaViewModel
import ru.genbach.chat.remote.service.ApiService
import ru.genbach.chat.ui.account.AccountActivity
import ru.genbach.chat.ui.core.PermissionManager
import ru.genbach.chat.ui.home.HomeActivity
import ru.genbach.chat.ui.home.MessagesActivity
import ru.genbach.chat.ui.login.LoginActivity
import ru.genbach.chat.ui.register.RegisterActivity
import ru.genbach.chat.ui.user.UserActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator     //  Вспомогательный класс. Для запуска activity.
@Inject constructor(
    private val authenticator: Authenticator,           //  объект
    private val permissionManager: PermissionManager    //  объект для работы с разрешениями. Тип:
) {
    //  в зависимости от наличия авторизованной сессии, запускает HomeActivity (если true) или LoginActivity (если false).
    //  Принимает Context. Ничего не возвращает.
    //  теперь, вместо получения результата из authenticator.userLoggeId, передаем туда функцию, обрабатывающую ответ.
    //  Логика обработки true/false не изменена.
    fun showMain(context: Context) {
        authenticator.userLoggedIn {
            if (it) showHome(context, false) else showLogin(context, false)
        }
    }
    //  запускает LoginActivity. Принимает Context, Boolean: newTask – определяет, очищать(если true),
    //  или не очищать(если false) backstack приложения. Ничего не возвращает.
    fun showLogin(context: Context, newTask: Boolean = true) = context.startActivity<LoginActivity>(newTask = newTask)

    fun showSignUp(context: Context) = context.startActivity<RegisterActivity>()    //  запускает Принимает Context. Ничего не возвращает.
    //  запускает Принимает Context, Boolean: newTask. Ничего не возвращает.
    fun showHome(context: Context, newTask: Boolean = true) = context.startActivity<HomeActivity>(newTask = newTask)


    fun showEmailNotFoundDialog(context: Context, email: String) {  //  перенесена из
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.message_promt_app))

            .setPositiveButton(android.R.string.yes) { dialog, which ->
                showEmailInvite(context, email)
            }

            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
    //   формирует и запускает intent для перехода в приложение почты. Принимает Context, String: email. Ничего не возвращает.
    private fun showEmailInvite(context: Context, email: String) {
        val appPackageName = context.packageName
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.message_subject_promt_app))
        emailIntent.putExtra(
            Intent.EXTRA_TEXT, context.resources.getString(R.string.message_text_promt_app) + " "
                    + context.resources.getString(R.string.url_google_play) + appPackageName
        )
        context.startActivity(Intent.createChooser(emailIntent, "Отправить"))
    }


    fun showAccount(context: Context) { //  запускает Принимает Context. Ничего не возвращает.
        context.startActivity<AccountActivity>()
    }

    //  запускает UserActivity. Передает параметры пользователя через Bundle. Принимает: Context, FriendEntity.
    fun showUser(context: Context, friendEntity: FriendEntity) {
        val bundle = Bundle()
        bundle.putString(ApiService.PARAM_IMAGE, friendEntity.image)
        bundle.putString(ApiService.PARAM_NAME, friendEntity.name)
        bundle.putString(ApiService.PARAM_EMAIL, friendEntity.email)
        bundle.putString(ApiService.PARAM_STATUS, friendEntity.status)
        context.startActivity<UserActivity>(args = bundle)
    }
    //  запускает MessageActivity. Передает параметры контакта через Bundle. Принимает: String: contactId, contactName; Context.
    fun showChatWithContact(contactId: Long, contactName: String, context: Context) {
        val bundle = Bundle()
        bundle.putLong(ApiService.PARAM_CONTACT_ID, contactId)
        bundle.putString(ApiService.PARAM_NAME, contactName)
        context.startActivity<MessagesActivity>(args = bundle)
    }

    //  показывает диалог с выбором источника изображения. Запрашивает разрешения для камеры и записи файлов через PermissionManager.
    //  Принимает AppCompatActivity и функцию высшего порядка onPick(принимает Boolean, возвращает Unit.
    //  Нужна для информирования вызывающего о выборе пользователем источника изображения.). Ничего не возвращает.
    fun showPickFromDialog(activity: AppCompatActivity, onPick: (fromCamera: Boolean) -> Unit) {
        val options = arrayOf<CharSequence>(
            activity.getString(R.string.camera),
            activity.getString(R.string.gallery)
        )

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(activity.getString(R.string.pick))
        builder.setItems(options) { _, item ->
            when (options[item]) {
                activity.getString(R.string.camera) -> {
                    permissionManager.checkCameraPermission(activity) {
                        onPick(true)
                    }
                }
                activity.getString(R.string.gallery) -> {
                    permissionManager.checkWritePermission(activity) {
                        onPick(false)
                    }
                }
            }
        }
        builder.show()
    }
    //  запускает активити камеры. Передает ссылку на созданный для камеры файл изображения.
    //  Принимает:AppCompatActivity, Uri. Ничего не возвращает.
    fun showCamera(activity: AppCompatActivity, uri: Uri) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        activity.startActivityForResult(intent, MediaViewModel.CAPTURE_IMAGE_REQUEST_CODE)
    }
    //  запускает активити галереи. Принимает AppCompatActivity. Ничего не возвращает.
    fun showGallery(activity: AppCompatActivity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"

        activity.startActivityForResult(intent, MediaViewModel.PICK_IMAGE_REQUEST_CODE)
    }
}
    //  вспомогательная extension функция, которая запускает activity. Формирует intent, при необходимости передает в него
    //  флаги очищения стека (addFlags(…)) и аргументы (putExtra(…)). Присутствует параметризированный вещественный тип T,
    //  который представляет из себя класс activity. Принимает Boolean: newTask; Bundle: args. Ничего не возвращает.
private inline fun <reified T> Context.startActivity(newTask: Boolean = false, args: Bundle? = null) {
    this.startActivity(Intent(this, T::class.java).apply {
        if (newTask) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        putExtra("args", args)
    })
}
