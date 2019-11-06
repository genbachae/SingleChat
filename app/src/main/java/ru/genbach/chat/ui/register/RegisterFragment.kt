package ru.genbach.chat.ui.register

import android.os.Bundle
import android.view.View
import ru.genbach.chat.R
import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.presentation.viewmodel.AccountViewModel
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseFragment
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment() {   //  Фрагмент. Для отображения регистрации. Наследуется от: BaseFragment.
    //  имплементация id макета регистрации из BaseFragment. Хранит id макета регистрации. Тип: Int.
    override val layoutId = R.layout.fragment_register
    //  переопределение id строки для заголовка тулбара. Тип:
    override val titleToolbar = R.string.register

    private lateinit var accountViewModel: AccountViewModel     //  объект
    //  переопределение метода жизненного цикла фрагмента. Выполняется инъекция компонента (App.appComponent.inject(…)) и
    //  инициализация accountViewModel (accountViewModel = viewModel(…); внутри передаваемой функции вызываются методы
    //  LifecycleOwneronSuccess(…) и onFailure(…), в параметры которых передаются данные и
    //  функции для их обработки(при помощи оператора “::”). Принимает Bundle. Ничего не возвращает.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)

        accountViewModel = viewModel {
            onSuccess(registerData, ::handleRegister)
            onSuccess(accountData, ::handleLogin)
            onFailure(failureData, ::handleFailure)
        }
    }
    //  переопределение метода жизненного цикла фрагмента. Выполняется присвоение кнопке слушателя нажатия(btnNewMembership.setOnClickListener(…)).
    //  При нажатии выполняется метод регистрации. Принимает View и Bundle. Ничего не возвращает.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   //   добавлен слушатель для кнопки Уже есть аккаунт.
        super.onViewCreated(view, savedInstanceState)

        btnNewMembership.setOnClickListener {
            register()
        }

        btnAlreadyHaveAccount.setOnClickListener {
            activity?.finish()
        }
    }
    //  проверяет, допустимо ли введены данные регистрации(email, имя пользователя, пароль).
    //  Делегирует проверку совпадения паролей методу validatePasswords(). Возвращает true, если все поля введены верно.
    //  Ничего не принимает. Возвращает Boolean.
    private fun validateFields(): Boolean {
        val allFields = arrayOf(etEmail, etPassword, etConfirmPassword, etusername)
        var allValid = true
        for (field in allFields) {
            allValid = field.testValidity() && allValid
        }
        return allValid && validatePasswords()
    }
    //  проверяет, совпадают ли поля ввода пароля(etPassword) и подтверждения пароля(etConfirmPassword).
    //  Возвращает true, если пароли совпадают. Ничего не принимает. Возвращает Boolean.
    private fun validatePasswords(): Boolean {
        val valid = etPassword.text.toString() == etConfirmPassword.text.toString()
        if (!valid) {
            showMessage(getString(R.string.error_password_mismatch))
        }
        return valid
    }
    //  делегирует выполнение регистрации объекту accountViewModel. Прячет клавиатуру(hideSoftKeyboard()),
    //  вызывает валидацию полей(validateFields()), показывает индикатор загрузки(showProgress()). Ничего не принимает. Ничего не возвращает.
    private fun register() {
        hideSoftKeyboard()

        val allValid = validateFields()

        if (allValid) {
            showProgress()

            accountViewModel.register(
                etEmail.text.toString(),
                etusername.text.toString(),
                etPassword.text.toString()
            )
        }
    }
    //  обработка авторизации. Запускает домашнее activity. Принимает AccountEntity. Ничего не возвращает.
    private fun handleLogin(accountEntity: AccountEntity?) {
        hideProgress()
        activity?.let {
            navigator.showHome(it)
            it.finish()
        }
    }
    //  вызывается в случае успеха регистрации. Прячет индикатор загрузки. Показывает сообщение «Аккаунт создан». Принимает Ничего не возвращает.
    //  выполняет автоматическую авторизацию при успешной регистрации.
    private fun handleRegister(none: None? = None()) {
        accountViewModel.login(etEmail.text.toString(), etPassword.text.toString())
    }
}