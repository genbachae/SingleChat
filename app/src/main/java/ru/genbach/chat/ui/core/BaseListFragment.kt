package ru.genbach.chat.ui.core

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.genbach.chat.R

abstract class BaseListFragment : BaseFragment() {  //  Базовый фрагмент. Для выделения поведения фрагментов содержащих список.

    private lateinit var recyclerView: RecyclerView     //  вью списка. Тип: RecyclerView.
    //  Модификатор доступа поля var lm: LayoutManager изменен на protected, что позволяет изменять его в дочерних классах.
    protected lateinit var lm: RecyclerView.LayoutManager   //  объект

    protected abstract val viewAdapter: BaseAdapter<*>      //  абстрактный объект адаптера. Будет имплементирован в дочерних классах. Тип:

    override val layoutId = R.layout.fragment_list          //  переопределенный id макета фрагмента. Присвоен макет содержащий список. Тип: Int.


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   //  инициализация вью списка, присвоение адаптера.
        super.onViewCreated(view, savedInstanceState)
        lm = LinearLayoutManager(context)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = viewAdapter
        }
    }

    protected fun setOnItemClickListener(func: (Any?, View) -> Unit) { //   сеттер слушателя нажатий для элементов списка.
        viewAdapter.setOnClick(func)
    }

    protected fun setOnItemLongClickListener(func: (Any?, View) -> Unit) {  //   сеттер слушателя нажатий для элементов списка.
        viewAdapter.setOnClick({_,_ ->}, longClick = func)
    }
}
