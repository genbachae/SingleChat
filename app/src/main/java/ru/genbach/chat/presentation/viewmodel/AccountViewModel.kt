package ru.genbach.chat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import ru.genbach.chat.domain.account.*
import ru.genbach.chat.domain.type.None
import javax.inject.Inject
//  Пользователь нажимает кнопку регистрации. UI обращается к ViewModel, который выполняет регистрацию и сообщает UI, что показывать.
class AccountViewModel @Inject constructor( //  Класс ViewModel. Для выполнения регистрации и хранения ответа сервера.
    val registerUseCase: Register,      //  объект для выполнения регистрации. Тип:
    val loginUseCase: Login,            //  use case для авторизации. Тип:
    val getAccountUseCase: GetAccount,  //  use case для получения аккаунта. Тип:
    val logoutUseCase: Logout,          //  usecase для выполнения выхода из аккаунта. Тип:
    val editAccountUseCase: EditAccount
) : BaseViewModel() {

    var registerData: MutableLiveData<None> = MutableLiveData()    //  объект, хранящий данные регистрации(ответ сервера). Тип: MutableLiveData<None>.
    var accountData: MutableLiveData<AccountEntity> = MutableLiveData()     //  хранит данные аккаунта. Тип: MutableLiveData<AccountEntity>.
    var editAccountData: MutableLiveData<AccountEntity> = MutableLiveData()
    var logoutData: MutableLiveData<None> = MutableLiveData()               //  хранит состояние logout. Тип: MutableLiveData<None>.
    //  делегирует выполнение регистрации UseCase-классу Register (registerUseCase(…); благодаря перегруженному в классе UseCase оператору
    //  вызова invoke, выражение registerUseCase(…) транслируется в registerUseCase.invoke(…)). Принимает String: email, name, password.
    //  Ничего не возвращает.       {Для регистрации и хранения ответа сервера.}
    fun register(email: String, name: String, password: String) {
        registerUseCase(Register.Params(email, name, password)) {
            it.either(::handleFailure, ::handleRegister) }
    }
    //  выполняет авторизацию. Записывает данные в accountData. Принимает String: email, password. Ничего не возвращает.
    fun login(email: String, password: String) {
        loginUseCase(Login.Params(email, password)) {
            it.either(::handleFailure, ::handleAccount)
        }
    }
    //  получает текущий аккаунт. Записывает данные в accountData. Ничего не принимает. Ничего не возвращает.
    fun getAccount() {
        getAccountUseCase(None()) { it.either(::handleFailure, ::handleAccount) }
    }
    //  выполняет выход из аккаунта. Записывает состояние в logoutData. Ничего не принимает. Ничего не возвращает.
    fun logout() {
        logoutUseCase(None()) { it.either(::handleFailure, ::handleLogout) }
    }
    //   выполняет изменения данных пользователя. Принимает AccountEntity. Ничего не возваращает.
    fun editAccount(entity: AccountEntity) {
        editAccountUseCase(entity) { it.either(::handleFailure, ::handleEditAccount) }
    }

    //  сеттер, присваивающий данные регистрации(ответ сервера). Принимает None. Ничего не возвращает.
    private fun handleRegister(none: None) {
        this.registerData.value = none
    }
    //  сеттер, присваивающий данные аккаунта. Принимает AccountEntity. Ничего не возвращает.
    private fun handleAccount(account: AccountEntity) {
        this.accountData.value = account
    }

    private fun handleEditAccount(account: AccountEntity) {    //  сеттер для
        this.editAccountData.value = account
    }
    //  сеттер, присваивающий состояние logout. Принимает None. Ничего не возвращает.
    private fun handleLogout(none: None) {
        this.logoutData.value = none
    }
    //   переопределение функции Android-класса ViewModel, которая вызывается при уничтожении фрагмента/активити.
    //   Выполняется отмена всех работ (registerUseCase.unsubscribe()). Ничего не принимает. Ничего не возвтращает.
    override fun onCleared() {
        super.onCleared()
        registerUseCase.unsubscribe()
        loginUseCase.unsubscribe()
        getAccountUseCase.unsubscribe()
        logoutUseCase.unsubscribe()
    }
}