package ru.genbach.chat.ui.core

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {       //  Вспомогательный класс. Для работы с датой.

    @JvmStatic
    @BindingAdapter("date")
    //  байндинг адаптер, который преобразовывает Long в читаемый формат даты(String).
    //  Метод сработает при вызове app:date у TextView в макете. Принимает Long. Ничего не возвращает.
    fun TextView.setDate(date: Long) {
        text = date.parseDate()
    }
}
//  преобразовывает Long в читаемую дату в зависимости от текущего времени.
//  Примеры: 13:25(сегодня), Вчера в 13:25(вчера), 17 октября(в этом году), 17.10.19(остальное). Ничего не принимает. Возвращает String.
fun Long.parseDate(): String {
    val currentLocale = Locale.getDefault()

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    var sdf = SimpleDateFormat("dd.MM.yy", currentLocale)

    if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
        calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
    ) {
        sdf = SimpleDateFormat("H:mm", currentLocale)
    } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1 &&
        calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
        sdf = SimpleDateFormat("Вчера в H:mm", currentLocale)
    } else if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
        sdf = SimpleDateFormat("d MMM", currentLocale)
    }

    return sdf.format(Date(this))
}