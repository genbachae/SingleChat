package ru.genbach.chat.ui.login

import android.os.Bundle
import android.view.View
import ru.genbach.chat.R
import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.presentation.viewmodel.AccountViewModel
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseFragment
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.etEmail
import kotlinx.android.synthetic.main.fragment_register.etPassword

class LoginFragment : BaseFragment() {      //  Для отображения авторизации.

    override val layoutId: Int = R.layout.fragment_login    //  имплементация id макета регистрации из BaseFragment. Хранит id макета регистрации. Тип: Int.
    override val titleToolbar = R.string.screen_login       //  переопределение id строки для заголовка тулбара. Тип: Int.

    private lateinit var accountViewModel: AccountViewModel     //  объект

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)

        //  инициализация accountViewModel, установка слушателя для accountData и failureData.
        accountViewModel = viewModel {
            onSuccess(accountData, ::renderAccount)
            onFailure(failureData, ::handleFailure)
        }
    }
    //  присвоение слушателей для кнопок авторизации и регистрации.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            validateFields()
        }

        btnRegister.setOnClickListener {
            activity?.let { navigator.showSignUp(it) }
        }
    }
    //  проверяет, допустимо ли введены данные авторизации(email и пароль).
    //  Если все поля введены верно – выполняет авторизацию. Ничего не принимает. Ничего не возвращает.
    private fun validateFields() {
        hideSoftKeyboard()
        val allFields = arrayOf(etEmail, etPassword)
        var allValid = true
        for (field in allFields) {
            allValid = field.testValidity() && allValid
        }
        if (allValid) {
            login(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    private fun login(email: String, password: String) {    //  выполняет авторизацию. Принимает String: email, password. Ничего не возвращает.
        showProgress()
        accountViewModel.login(email, password)
    }
    //  обрабатывает получение аккаунта. Запускает домашнее activity. Принимает AccountEntity. Ничего не возвращает.
    private fun renderAccount(account: AccountEntity?) {
        hideProgress()
        activity?.let {
            navigator.showHome(it)
            it.finish()
        }
    }
}