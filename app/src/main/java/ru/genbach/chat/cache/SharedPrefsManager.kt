package ru.genbach.chat.cache

import android.content.SharedPreferences
import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.type.Failure
import javax.inject.Inject
//  Класс для работы с SharedPreferences. Содержит: объект SharedPreferences(val prefs), вспомогательные константы(val ACCOUNT_TOKEN),
//  функции для редактирования(fun saveToken(…)) и получения(fun getToken()) токена из SharedPreferences.
//  Сохранение и восстановление данных.

class SharedPrefsManager @Inject constructor(private val prefs: SharedPreferences) {
    companion object {
        const val ACCOUNT_TOKEN = "account_token"
        const val ACCOUNT_ID = "account_id"
        const val ACCOUNT_NAME = "account_name"
        const val ACCOUNT_EMAIL = "account_email"
        const val ACCOUNT_STATUS = "account_status"
        const val ACCOUNT_DATE = "account_date"
        const val ACCOUNT_IMAGE = "account_image"
        const val ACCOUNT_PASSWORD = "account_password"
    }
    //  сохраняет токен в SharedPreferences(edit().putString(…)). Принимает строку token. Возвращает Either<Failure, None>.
    fun saveToken(token: String): Either<Failure, None> {
        prefs.edit().apply {
            putString(ACCOUNT_TOKEN, token)
        }.apply()

        return Either.Right(None())
    }
    //  восстанавливает токен из SharedPreferences(getString(…)). Ничего не принимает. Возвращает Either<Failure, String>.
    fun getToken(): Either<Failure, String> {
        return Either.Right(prefs.getString(ACCOUNT_TOKEN, ""))
    }
    //  сохраняет поля аккаунта в Принимает AccountEntity. Возвращает Either<Failure, None>.
    fun saveAccount(account: AccountEntity): Either<Failure, None> {
        prefs.edit().apply {
            putSafely(ACCOUNT_ID, account.id)
            putSafely(ACCOUNT_NAME, account.name)
            putSafely(ACCOUNT_EMAIL, account.email)
            putSafely(ACCOUNT_TOKEN, account.token)
            putString(ACCOUNT_STATUS, account.status)
            putSafely(ACCOUNT_DATE, account.userDate)
            putSafely(ACCOUNT_IMAGE, account.image)
            putSafely(ACCOUNT_PASSWORD, account.password)
        }.apply()

        return Either.Right(None())
    }
    //  проверяет наличие аккаунта в SharedPreferences (if (id == 0)) и при его отсутствии возвращает NoSavedAccountsError.
    //  Получает аккаунт из SharedPreferences. Ничего не принимает. Возвращает Either<Failure, AccountEntity>.
    fun getAccount(): Either<Failure, AccountEntity> {
        val id = prefs.getLong(ACCOUNT_ID, 0)

        if (id == 0L) {
            return Either.Left(Failure.NoSavedAccountsError)
        }

        val account = AccountEntity(
            prefs.getLong(ACCOUNT_ID, 0),
            prefs.getString(ACCOUNT_NAME, ""),
            prefs.getString(ACCOUNT_EMAIL, ""),
            prefs.getString(ACCOUNT_TOKEN, ""),
            prefs.getString(ACCOUNT_STATUS, ""),
            prefs.getLong(ACCOUNT_DATE, 0),
            prefs.getString(ACCOUNT_IMAGE, ""),
            prefs.getString(ACCOUNT_PASSWORD, "")
        )

        return Either.Right(account)
    }

    fun removeAccount(): Either<Failure, None> {    //  удаляет аккаунт из Ничего не принимает. Возвращает Either<Failure, None>.
        prefs.edit().apply {
            remove(ACCOUNT_ID)
            remove(ACCOUNT_NAME)
            remove(ACCOUNT_EMAIL)
            remove(ACCOUNT_STATUS)
            remove(ACCOUNT_DATE)
            remove(ACCOUNT_IMAGE)
            remove(ACCOUNT_PASSWORD)
        }.apply()

        return Either.Right(None())
    }
    //  возвращаемый тип изменен на Either<Failure, Boolean>. Результат выполнения функции обернут в Right(…).
    fun containsAnyAccount(): Either<Failure, Boolean> {    //  проверяет наличие аккаунта в Ничего не принимает.
        val id = prefs.getLong(ACCOUNT_ID, 0)
        return Either.Right(id != 0L)
    }

}

fun SharedPreferences.Editor.putSafely(key: String, value: Long?) {
    if (value != null && value != 0L) {
        putLong(key, value)
    }
}

fun SharedPreferences.Editor.putSafely(key: String, value: String?) {
    if (value != null && value.isNotEmpty()) {
        putString(key, value)
    }
}