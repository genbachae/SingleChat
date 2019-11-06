package ru.genbach.chat.ui.user

import android.os.Bundle
import android.view.View
import ru.genbach.chat.R
import ru.genbach.chat.remote.service.ApiService
import ru.genbach.chat.ui.core.BaseFragment
import ru.genbach.chat.ui.core.GlideHelper
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : BaseFragment() {       //  Фрагмент. Для отображения данных пользователя.
    override val layoutId = R.layout.fragment_user  //   имплементация id макета аккаунта. Тип: Int.

    override val titleToolbar = R.string.screen_user    //  переопределение id строки для заголовка тулбара. Тип:
    //  получает данные пользователя из intent. Отображает данные пользователя.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        base {
            val args = intent.getBundleExtra("args")
            if (args != null) {
                val image = args.getString(ApiService.PARAM_IMAGE)
                val name = args.getString(ApiService.PARAM_NAME)
                val email = args.getString(ApiService.PARAM_EMAIL)
                val status = args.getString(ApiService.PARAM_STATUS)

                GlideHelper.loadImage(requireContext(), image, imgPhoto, R.drawable.ic_account_circle)

                tvName.text = name
                tvEmail.text = email
                tvStatus.text = status

                if (tvStatus.text.isEmpty()) {
                    tvStatus.visibility = View.GONE
                    tvHintStatus.visibility = View.GONE
                }
            }
        }
    }
}