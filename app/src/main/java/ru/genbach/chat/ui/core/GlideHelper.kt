package ru.genbach.chat.ui.core

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import ru.genbach.chat.remote.service.ServiceFactory

/*Пример:
Сервер отдает неполную ссылку на изображение(без доменного имени). GlideHelper преобразовывает ее в полную, добавляя доменное имя.*/

object GlideHelper {    //  Вспомогательный класс. Для загрузки и отображения изображений.
    //  использует Glide для загрузки и отображения изображений. Преобразовывает path для загрузки изображения.
    //  Принимает Context, String: path, ImageView, Drawable/Int: placeholder (отображается во время загрузки изображения). Ничего не возвращает.
    fun loadImage(context: Context, path: String?, iv: ImageView, placeholder: Drawable = iv.drawable) {
        val imgPath = ServiceFactory.SERVER_URL + path?.replace("..", "")

        Glide.with(context)
            .load(imgPath)
            .placeholder(placeholder)
            .error(placeholder)
            .into(iv)
    }

    fun loadImage(context: Context, path: String?, iv: ImageView, placeholder: Int) {
        loadImage(context, path, iv, context.resources.getDrawable(placeholder))
    }

    @JvmStatic
    @BindingAdapter("profileImage")
    //  байндинг адаптер, который устанавливает сеттер для поля profileImage.
    //  Метод сработает при вызове app:profileImage у ImageView в макете. Принимает String. Ничего не возвращает.
    fun ImageView.loadImage(image: String?) {
        loadImage(this.context, image, this)
    }
}
