package ru.genbach.chat.ui.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.genbach.chat.R
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.ui.core.navigation.Navigator
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
/* Все активити приложения содержат фрагмент и кастомный тулбар.
Для того чтобы не писать их настройку в каждом активити, мы выносим это поведение в отдельный базовый класс.*/
abstract class BaseActivity : AppCompatActivity() {     //  Базовый класс. Для выделения общего поведения всех активити.
    //  абстрактный объект фрагмента, который отображается на экране. Будет имплементирован в дочерних активити. Тип: BaseFragment.
    abstract var fragment: BaseFragment         //  изменено на var для поддержки изменения фрагмента.

    @Inject
    //  фабрика для создания ViewModel. Инициализируется при помощи Dagger (@Inject). Тип: Factory.
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var permissionManager: PermissionManager   //  объект для работы с разрешениями. Тип:

    open val contentId = R.layout.activity_layout       //  хранит id макета для Тип: Int.
    //  переопределения метода жизненного цикла активити. Вызывается установка общего для всех активити layout’a(setContentView(…)),
    //  установка кастомного тулбара(setSupportActionBar(…)) и добавление фрагмента (addFragment(…)).
    //  Принимает сохраненное состояние Ничего не возвращает.
    //  setContentView вынесен в openfunsetupContent(), для возможности переопределения установки контента активити.
    //  Будет использоваться для байндинга данных.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContent()

        setSupportActionBar(toolbar)
        addFragment(savedInstanceState)
    }

    open fun setupContent() {
        setContentView(contentId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //  делегирует фрагменту обработку результата активити.
        super.onActivityResult(requestCode, resultCode, data)

        fragment.onActivityResult(requestCode, resultCode, data)
    }
    //  переопределение метода активити, выполняющегося в момент нажатия кнопки «Назад».
    //  Вызывает метод onBackPressed() у текущего фрагмента. Ничего не принимает. Ничего не возвращает.
    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            R.id.fragmentContainer
        ) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }
    //  добавляет фрагмент, используя extension функцию высшего порядка FragmentManager.inTransaction(…).
    //  При наличии сохраненного состояния фрагмент не добавляется(оператор Элвиса). Принимает Bundle. Возвращает
    fun addFragment(savedInstanceState: Bundle? = null, fragment: BaseFragment = this.fragment) {   //  добавлен параметр fragment.
        savedInstanceState ?: supportFragmentManager.inTransaction {
            add(R.id.fragmentContainer, fragment)
        }
    }

    fun replaceFragment(fragment: BaseFragment) {   //  замена фрагмента активити. Принимает BaseFragment. Ничего не возвращает.
        this.fragment = fragment
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, fragment)
        }
    }

    //  при помощи метода progressStatus(…), показывает индикатор загрузки. Ничего не принимает. Ничего не возвращает.
    fun showProgress() = progressStatus(View.VISIBLE)
    //  при помощи метода progressStatus(…), скрывает индикатор загрузки. Ничего не принимает. Ничего не возвращает.
    fun hideProgress() = progressStatus(View.GONE)
    //  устанавливает видимость индикатора загрузки. Принимает статус видимости Int. Ничего не возвращает.
    fun progressStatus(viewStatus: Int) {
        toolbar_progress_bar.visibility = viewStatus
    }

    //  скрывает клавиатуру. Ничего не принимает. Ничего не возвращает.
    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
    //  в зависимости от типа ошибки(failure), определяет, какое сообщение нужно показать на экране.
    //  Делегирует показ сообщения методу showMessage(…). Принимает Failure. Ничего не возвращает.
    //  добавлена обработка ошибки при выборе изображения аккаунта (FilePickError).
    //  добавлена обработка ошибок: AlreadyFriendError и
    open fun handleFailure(failure: Failure?) {     //  добавлена обработка ошибок: AuthError и TokenError.
        hideProgress()
        when (failure) {
            is Failure.NetworkConnectionError -> showMessage(getString(R.string.error_network))
            is Failure.ServerError -> showMessage(getString(R.string.error_server))
            is Failure.EmailAlreadyExistError -> showMessage(getString(R.string.error_email_already_exist))
            is Failure.AuthError -> showMessage(getString(R.string.error_auth))
            is Failure.TokenError -> navigator.showLogin(this)
            is Failure.AlreadyFriendError -> showMessage(getString(R.string.error_already_friend))
            is Failure.AlreadyRequestedFriendError -> showMessage(getString(R.string.error_already_requested_friend))
            is Failure.FilePickError -> showMessage(getString(R.string.error_picking_file))
        }
    }
    //  показывает тост с текстом. Принимает String. Ничего не возвращает.
    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    //  функция, которая создает ViewModel и применяет к ней функцию высшего порядка, переданную в параметрах(vm.body()).
    //  Присутствует параметризированный вещественный(refied) тип T, который наследуется от ViewModel.
    //  Принимает: функцию body (Принимает T.(), ничего не возвращает). Возвращает T.
    inline fun <reified T : ViewModel> viewModel(body: T.() -> Unit): T {
        val vm = ViewModelProviders.of(this, viewModelFactory)[T::class.java]
        vm.body()
        return vm
    }

    //  делегирует PermissionManager обработку результата запрашивания разрешений.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.requestObject?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
//  extension функция, которая передает функцию func в тело FragmentTransaction.beginTransaction().
//  Принимает функцию func(Принимает(), возвращает FragmentTransaction). Возвращает Int.
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()
//  extenstion функция, которая передает функцию body в тело активити BaseActivity, позволяя вызывать ее методы.
//  Принимает функцию base(Принимает BaseActivity.(), ничего не возвращает. Ничего не возвращает.
inline fun Activity?.base(block: BaseActivity.() -> Unit) {
    (this as? BaseActivity)?.let(block)
}

