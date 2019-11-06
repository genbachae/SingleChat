package ru.genbach.chat.ui.login

import ru.genbach.chat.ui.core.BaseActivity
import ru.genbach.chat.ui.core.BaseFragment

class LoginActivity : BaseActivity() {      //  Для отображения фрагмента авторизации.
    //  имплементация абстрактного объекта фрагмента. Тип: LoginFragment.
    override var fragment: BaseFragment = LoginFragment()   //  заменен на
}
