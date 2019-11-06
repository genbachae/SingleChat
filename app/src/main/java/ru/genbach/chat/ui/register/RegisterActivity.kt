package ru.genbach.chat.ui.register

import ru.genbach.chat.ui.core.BaseActivity
import ru.genbach.chat.ui.core.BaseFragment

class RegisterActivity : BaseActivity() {   //  Активити. Для отображения фрагмента регистрации. Наследуется от: Общего класса BaseActivity.
    //  имплементация абстрактного объекта фрагмента. Тип: RegisterFragment.
    override var fragment: BaseFragment = RegisterFragment()    //  заменен на var
}
