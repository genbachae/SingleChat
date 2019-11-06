package ru.genbach.chat.ui.user

import ru.genbach.chat.ui.core.BaseActivity
import ru.genbach.chat.ui.core.BaseFragment

class UserActivity : BaseActivity() {   // Для отображения фрагмента пользователя.
    override var fragment: BaseFragment = UserFragment()        //  имплементация абстрактного объекта фрагмента. Тип: UserFragment.
}
