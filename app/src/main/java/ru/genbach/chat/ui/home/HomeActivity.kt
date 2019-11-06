package ru.genbach.chat.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import ru.genbach.chat.R
import ru.genbach.chat.databinding.ActivityNavigationBinding
import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.presentation.viewmodel.AccountViewModel
import ru.genbach.chat.presentation.viewmodel.FriendsViewModel
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseActivity
import ru.genbach.chat.ui.core.BaseFragment
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess
import ru.genbach.chat.ui.firebase.NotificationHelper
import ru.genbach.chat.ui.friends.FriendRequestsFragment
import ru.genbach.chat.ui.friends.FriendsFragment
import javax.inject.Inject


class HomeActivity : BaseActivity() {   //  Активити. Для отображения фрагмента чатов и меню навигации.

    override var fragment: BaseFragment = ChatsFragment()   //  имплементация абстрактного объекта фрагмента. Тип: ChatsFragment.

    override val contentId = R.layout.activity_navigation   //  переопределение id макета Тип: Int.

    private lateinit var accountViewModel: AccountViewModel     //  объект

    private lateinit var binding: ActivityNavigationBinding    //   байндинг. Тип: Сгенерированный класс ActivityNavigationBinding.

    @Inject
    lateinit var friendsViewModel: FriendsViewModel //  объект FriendsViewModel для взаимодействия с друзьями.


    override fun setupContent() {   //  переопределение метода Выполняется инициализация поля binding.
        //  Теперь для доступа к макету нужно использовать поле binding. Для этого изменим:
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation)
    }

    //  вызов полей requestContainer, btnLogout, btnChats, btnAddFriend, containerAddFriend, btnAdd,
    //  btnFriends, btnRequests, profileContainer изменим на navigation.*
    // 6й вызов getAccount() перенесен в onResume(). Добавлена обработка интента нотификации:
    // в случае если тип нотификации ADD_FRIEND, открывается дровер и отображается фрагменг с списком приглашений.
    // Добавлен вызов метода showAccount() при нажатии на профиль в дровере.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        App.appComponent.inject(this)
        //  инициализация accountViewModel и установка слушателей на accountData, logoutData, failureData.
        //  Получение аккаунта(accountViewModel.getAccount()). Установка иконки меню навигации (supportActionBar.setHomeAsUpIndicator(…)).
        //  Установка слушателя на кнопку logout в меню навигации.
        accountViewModel = viewModel {
            onSuccess(accountData, ::handleAccount)
            onSuccess(logoutData, ::handleLogout)
            onFailure(failureData, ::handleFailure)
        }
        //  добавлена инициализация friendsViewModel, установка слушателей на addFriendsData, friendRequestsData, failureData.
        //  Установка слушателей на кнопки меню навигации: Чаты, Друзья, Пригласить друга, Входящие приглашения.
        //  При начатии на кнопки Пригласить друга и входящие приглашения, в меню добавляются соответствующие вложенные фрагменты.
        friendsViewModel = viewModel {
            onSuccess(addFriendData, ::handleAddFriend)
            onSuccess(friendRequestsData, ::handleFriendRequests)
            onFailure(failureData, ::handleFailure)
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.requestContainer, FriendRequestsFragment()).commit()

        val type: String? = intent.getStringExtra("type")
        when (type) {
            NotificationHelper.TYPE_ADD_FRIEND -> {
                openDrawer()
                friendsViewModel.getFriendRequests()
                binding.navigation.requestContainer.visibility = View.VISIBLE
            }
        }

        binding.navigation.btnLogout.setOnClickListener {
            accountViewModel.logout()
        }

        binding.navigation.btnChats.setOnClickListener {
            replaceFragment(ChatsFragment())
            closeDrawer()
        }

        binding.navigation.btnAddFriend.setOnClickListener {
            if (binding.navigation.containerAddFriend.visibility == View.VISIBLE) {
                binding.navigation.containerAddFriend.visibility = View.GONE
            } else {
                binding.navigation.containerAddFriend.visibility = View.VISIBLE
            }
        }

        binding.navigation.btnAdd.setOnClickListener {
            hideSoftKeyboard()
            showProgress()
            friendsViewModel.addFriend(binding.navigation.etEmail.text.toString())
        }

        binding.navigation.btnFriends.setOnClickListener {
            replaceFragment(FriendsFragment())
            closeDrawer()
        }
        //  в слушателе кнопки Входящие приглашения, при вызове метода getFriendRequests(…) передается true,
        //  что позволяет загружать данные приглашений из сети (по умолчанию false – из бд).
        binding.navigation.btnRequests.setOnClickListener {
            friendsViewModel.getFriendRequests(true)

            if (binding.navigation.requestContainer.visibility == View.VISIBLE) {
                binding.navigation.requestContainer.visibility = View.GONE
            } else {
                binding.navigation.requestContainer.visibility = View.VISIBLE
            }
        }

        binding.navigation.profileContainer.setOnClickListener {
            navigator.showAccount(this)
            Handler(Looper.getMainLooper()).postDelayed({
                closeDrawer()
            }, 200)
        }
    }
    //  настройка открытия/закрытия дровера при нажатии на иконку навигации.
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (binding.drawerLayout.isDrawerOpen(binding.navigation.navigationView)) {
                    binding.drawerLayout.closeDrawer(binding.navigation.navigationView)
                } else {
                    binding.drawerLayout.openDrawer(binding.navigation.navigationView)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        accountViewModel.getAccount()
    }

    private fun openDrawer() {  //  вспомогательные функции для открытия меню навигации.
        hideSoftKeyboard()
        binding.drawerLayout.openDrawer(binding.navigation.navigationView)
    }

    private fun closeDrawer(animate: Boolean = true) {  // вспомогательные функции для закрытия меню навигации.
        hideSoftKeyboard()
        binding.drawerLayout.closeDrawer(binding.navigation.navigationView, animate)
    }
    //  обработка полученного аккаунта. Установка имени, email и статуса аккаунта в меню навигации. Принимает AccountEntity. Ничего не возвращает.
    private fun handleAccount(accountEntity: AccountEntity?) {  //  содержимое блока let заменим на binding.navigation.account = it.
        accountEntity?.let {
            binding.navigation.account = it
        }
    }
    //  обработка выхода из аккаунта. Запуск activity авторизации. Принимает None. Ничего не возвращает.
    private fun handleLogout(none: None?) {
        navigator.showLogin(this)
        finish()
    }
    //  дополнительная обработка добавления друга. Скрывает фрагмент добавления друга.
    private fun handleAddFriend(none: None?) {
        binding.navigation.etEmail.text.clear()
        binding.navigation.containerAddFriend.visibility = View.GONE

        hideProgress()
        showMessage("Запрос отправлен.")
    }
    //  дополнительная обработка получения списка приглашений. Если список приглашений пуст – скрывает фрагмент списка приглашений из меню навигации.
    private fun handleFriendRequests(requests: List<FriendEntity>?) {
        if (requests?.isEmpty() == true) {
            binding.navigation.requestContainer.visibility = View.GONE
            if (binding.drawerLayout.isDrawerOpen(binding.navigation.navigationView)) {
                showMessage("Нет входящих приглашений.")
            }
        }
    }
    //  дополнительная обработка ошибок. При попытке добавления в друзья несуществующего пользователя,
    //  будет отображен диалог с возможность отправки приглашения непосредственно на почту.
    override fun handleFailure(failure: Failure?) {
        hideProgress()
        when (failure) {
            Failure.ContactNotFoundError -> navigator.showEmailNotFoundDialog(  // отображение диалога при попытке добавления несуществующего пользователя.
                this,
                binding.navigation.etEmail.text.toString()
            )
            else -> super.handleFailure(failure)
        }
    }

    //  переопределение нажатия кнопки Назад. Скрывает дровер, если он открыт.
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navigation.navigationView)) {
            hideSoftKeyboard()
            binding.drawerLayout.closeDrawer(binding.navigation.navigationView)
        } else {
            super.onBackPressed()
        }
    }
}
