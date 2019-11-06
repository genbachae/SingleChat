package ru.genbach.chat.ui.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

//  Для переноса логики инфлейта макета дочерним классам. Позволит использовать data binding.
//  Удален метод createHolder(…).
//  Реализация метода onCreateViewHolder(…) перенесена в дочерние классы.
//  Базовый класс адаптера. Присутствует параметризированный тип VH, наследующийся от BaseViewHolder.
abstract class BaseAdapter<VH : BaseAdapter.BaseViewHolder> : RecyclerView.Adapter<VH>() {  //  Для выделения поведения списков.

    var items: ArrayList<Any> = ArrayList()     //  элементы списка. Тип: ArrayList<Any>.

    var onClick: OnClick? = null                //  объект слушателя OnClick. слушатель нажатий. Тип: OnClick.

    override fun getItemCount(): Int {  //  возвращает количество элементов в списке. Ничего не принимает. Возвращает
        return items.size
    }
    //  заполняет с помощью функции holder.bind(…) макет элемента списка данными. Присваивает вьюхолдеру слушатель нажатий.
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))

        holder.onClick = onClick
    }

    //  возвращает из списка элемент. Ничего не принимает. Возвращает Any.
    fun getItem(position: Int): Any {
        return items[position]
    }

    //  добавляет элемент в список. Принимает: Any или List<Any>. Ничего не возвращает.
    fun add(newItem: Any) {
        items.add(newItem)
    }

    fun add(newItems: List<Any>) {
        items.addAll(newItems)
    }

    fun clear() {   //  очищает список.
        items.clear()
    }

    //  сеттер для поля onClick. Принимает функции высшего порядка для обычного(click: (Any, View) -> Unit)
    //  и длительного(longClick: (Any, View) -> Unit) нажатия. Ничего не возвращает.
    fun setOnClick(click: (Any?, View) -> Unit, longClick: (Any?, View) -> Unit = {_,_ ->}) {
        onClick = object : OnClick {
            override fun onClick(item: Any?, view: View) {
                click(item, view)
            }

            override fun onLongClick(item: Any?, view: View) {
                longClick(item, view)
            }
        }
    }

    interface OnClick {     //  интерфейс-слушатель нажатий на элементы списка.
        fun onClick(item: Any?, view: View)
        fun onLongClick(item: Any?, view: View)
    }
    //  Базовый класс вьюхолдера. Для выделения поведения вьюхолдеров.
    abstract class BaseViewHolder(protected val view: View) : RecyclerView.ViewHolder(view) {
        var onClick: OnClick? = null
        var item: Any? = null           //  элемент списка. Тип: Any.

        init {          //  блок инициализации, где вью присваиваются слушатели нажатий.
            view.setOnClickListener {
                onClick?.onClick(item, it)
            }
            view.setOnLongClickListener {
                onClick?.onLongClick(item, it)
                true
            }
        }
        //  абстрактная функция, заполняющая макет элемента списка данными. Принимает Ничего не возвращает.
        protected abstract fun onBind(item: Any)
        //  присваивает элемент списка вьюхолдеру. Делегирует заполнение вьюхолдера функции onBind(…). Принимает Ничего не возвращает.
        fun bind(item: Any) {
            this.item = item

            onBind(item)
        }

    }
}

