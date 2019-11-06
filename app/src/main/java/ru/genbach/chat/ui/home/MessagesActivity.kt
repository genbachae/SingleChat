package ru.genbach.chat.ui.home

import ru.genbach.chat.ui.core.BaseActivity
import ru.genbach.chat.ui.core.BaseFragment

class MessagesActivity : BaseActivity() {   //  Активити. Содержит фрагмент MessageFragment.
    override var fragment: BaseFragment = MessagesFragment()    //  объект фрагмента
}
