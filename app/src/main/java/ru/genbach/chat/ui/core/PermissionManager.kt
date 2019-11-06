package ru.genbach.chat.ui.core

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.github.kayvannj.permission_utils.Func
import com.github.kayvannj.permission_utils.PermissionUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor() {     //  Класс. Для работы с разрешениями.

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 10003     //  код запроса разрешения камеры.
        const val REQUEST_WRITE_PERMISSION = 10004      //  код запроса разрешения записи файлов.
    }

    var requestObject: PermissionUtil.PermissionRequestObject? = null   //  объект
    //  запрашивает разрешение на запись файлов. В случае если разрешение предоставлено, выполняет переданную функцию body.
    //  Принимает AppCompatActivity, функцию body(ничего не принимает, возвращает Unit). Ничего не возвращает.
    fun checkWritePermission(activity: AppCompatActivity, body: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestObject = PermissionUtil.with(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .ask(REQUEST_WRITE_PERMISSION) {
                    body()
                }
        } else {
            body()
        }
    }
    //   запрашивает разрешение на использование камеры. В случае если разрешение предоставлено, выполняет переданную функцию body.
    //   Принимает AppCompatActivity, функцию body(ничего не принимает, возвращает Unit). Ничего не возвращает.
    fun checkCameraPermission(activity: AppCompatActivity, body: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestObject = PermissionUtil.with(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .ask(REQUEST_CAMERA_PERMISSION) {
                    body()
                }
        } else {
            body()
        }
    }
}
    //  вспомогательная extension функция, выполняющая функцию granted при предоставлении разрешения.
    //  Принимает Int: code, функцию granted(ничего не принимает, возвращает Unit).
fun PermissionUtil.PermissionRequestObject.ask(code: Int, granted: () -> Unit): PermissionUtil.PermissionRequestObject? {
    return this.onAllGranted(object : Func() {
        override fun call() {
            granted()
        }
    }).ask(code)
}
