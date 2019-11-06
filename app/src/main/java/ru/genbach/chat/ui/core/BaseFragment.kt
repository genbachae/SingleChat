package ru.genbach.chat.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.genbach.chat.R
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.ui.core.navigation.Navigator
import javax.inject.Inject

abstract class BaseFragment : Fragment() {  //  Базовый класс. Для выделения общего поведения фрагментов.
    //  абстрактный объект, хранящий idlayout’а фрагмента. Будет имплементирован в дочерних классах. Тип:
    abstract val layoutId: Int          //  имплементация id макета регистрации из BaseFragment. Хранит id макета регистрации. Тип: Int.
    //  хранит id строки, которая будет отображаться в заголовке тулбара. Может быть имплементирован в дочерних классах. Тип: Int.
    open val titleToolbar = R.string.app_name
    //  определяет, будет ли показываться тулбар в конкретном фрагменте. Может быть имплементирован в дочерних классах. Тип: Boolean.
    open val showToolbar = true

    @Inject
    lateinit var navigator: Navigator

    @Inject
    //  фабрика для создания ViewModel. Инициализируется при помощи Dagger (@Inject). Тип: Factory.
    lateinit var viewModelFactory: ViewModelProvider.Factory
    //  переопределенный метод жизенного цикла фрагмента. Создает view, которая будет отображаться на экране(inflater.inflate(…)).
    //  Принимает: LayoutInflater, ViewGroup, Bundle. Возвращает:
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)
    }
    //  переопределенный метод жизненного цикла фрагмента. В зависимости значения showToolbar,
    //  показывает(true) или скрывает(false) тулбар. Устанавливает заголовок тулбара. Ничего не принимает. Ничего не возвращает.
    override fun onResume() {
        super.onResume()

        base {
            if (showToolbar) supportActionBar?.show() else supportActionBar?.hide()
            supportActionBar?.title = getString(titleToolbar)
        }
    }

    open fun onBackPressed() {}


    open fun updateProgress(status: Boolean?) { //  показывает или скрывает прогресс бар. Принимает Boolean. Ничего не возвращает.
        if (status == true) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    //  вспомогательная функция, которая вызывает такие же функции в BaseActivity.
    fun showProgress() = base { progressStatus(View.VISIBLE) }
    //  вспомогательная функция, которая вызывает такие же функции в BaseActivity.
    fun hideProgress() = base { progressStatus(View.GONE) }

    //  вспомогательная функция, которая вызывает такие же функции в BaseActivity.
    fun hideSoftKeyboard() = base { hideSoftKeyboard() }

    //  вспомогательная функция, которая вызывает такие же функции в BaseActivity.
    fun handleFailure(failure: Failure?) = base { handleFailure(failure) }
    //  вспомогательная функция, которая вызывает такие же функции в BaseActivity.
    fun showMessage(message: String) = base { showMessage(message) }

    //  вспомогательная функция, которая вызывает такие же функции в BaseActivity.
    inline fun base(block: BaseActivity.() -> Unit) {
        activity.base(block)
    }

    //  функция, которая создает ViewModel и применяет к ней функцию высшего порядка, переданную в параметрах(vm.body()).
    //  Присутствует параметризированный вещественный(refied) тип T, который наследуется от ViewModel.
    //  Принимает: функцию body (Принимает T.(), ничего не возвращает). Возвращает T.
    inline fun <reified T : ViewModel> viewModel(body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, viewModelFactory)[T::class.java]
        vm.body()
        return vm
    }
    //  закрывает фрагмент. Ничего не принимает. Ничего не возвращает.
    fun close() = fragmentManager?.popBackStack()
}
