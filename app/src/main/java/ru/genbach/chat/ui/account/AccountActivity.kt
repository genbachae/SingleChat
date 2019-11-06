package ru.genbach.chat.ui.account

import android.os.Bundle
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseActivity
import ru.genbach.chat.ui.core.BaseFragment

class AccountActivity : BaseActivity() {    //  Активити. Для отображения фрагмента аккаунта. Наследуется от: BaseActivity.
    override var fragment: BaseFragment = AccountFragment()     //  имплементация абстрактного объекта фрагмента. Тип: AccountFragment.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }
}
