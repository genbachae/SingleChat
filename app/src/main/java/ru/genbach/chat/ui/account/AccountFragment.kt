package ru.genbach.chat.ui.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import ru.genbach.chat.R
import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.presentation.viewmodel.AccountViewModel
import ru.genbach.chat.presentation.viewmodel.MediaViewModel
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseFragment
import ru.genbach.chat.ui.core.GlideHelper
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment() {    //  Фрагмент. Для отображения данных аккаунта.
    override val layoutId = R.layout.fragment_account   //  имплементация id макета аккаунта. Тип: Int.

    override val titleToolbar = R.string.screen_account                 //  переопределение id строки для заголовка тулбара.

    lateinit var accountViewModel: AccountViewModel         //  объект
    lateinit var mediaViewModel: MediaViewModel             //  объект

    var accountEntity: AccountEntity? = null                //  объект

    override fun onCreate(savedInstanceState: Bundle?) {    //  выполняет инъекцию компонента и инициализацию ViewModel.
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)

        accountViewModel = viewModel {
            onSuccess(accountData, ::handleAccount)
            onSuccess(editAccountData, ::handleEditingAccount)
            onFailure(failureData, ::handleFailure)
        }

        mediaViewModel = viewModel {
            onSuccess(cameraFileCreatedData, ::onCameraFileCreated)
            onSuccess(pickedImageData, ::onImagePicked)
            onSuccess(progressData, ::updateProgress)
            onFailure(failureData, ::handleFailure)
        }
    }
    //  получает результат выбора изображения(галерея и камера) и делегирует его обработку MediaViewModel.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mediaViewModel.onPickImageResult(requestCode, resultCode, data)
    }
    //  вызывает получение текущего аккаунта. Присваивает слушатели нажатия для кнопки «Обновить аккаунт»
    //  (выполняется валидация полей ввода и паролей, присвоение переменной accountEntity значений из полей ввода)
    //  и изображения пользователя(показывает диалог выбора источника фото(галерея или камера);
    //  при значении fromCameratrue: создает файл изображения; при значении fromCamerafalse: показывает активити с галереей).
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgress()

        accountViewModel.getAccount()

        btnEdit.setOnClickListener {
            view.clearFocus()
            val fieldsValid = validateFields()
            if (!fieldsValid) {
                return@setOnClickListener
            }

            val passwordsValid = validatePasswords()
            if (!passwordsValid) {
                return@setOnClickListener
            }

            showProgress()

            val email = etEmail.text.toString()
            val name = etName.text.toString()
            val status = etStatus.text.toString()
            val password = etNewPassword.text.toString()

            accountEntity?.let {
                it.email = email
                it.name = name
                it.status = status

                if (password.isNotEmpty()) {
                    it.password = password
                }

                accountViewModel.editAccount(it)
            }
        }

        imgPhoto.setOnClickListener {
            base {
                navigator.showPickFromDialog(this) { fromCamera ->
                    if (fromCamera) {
                        mediaViewModel.createCameraFile()
                    } else {
                        navigator.showGallery(this)
                    }
                }
            }
        }
    }
    //  выполняет валидацию полей текущего и нового пароля. Ничего не принимает. Возвращает Boolean.
    private fun validatePasswords(): Boolean {
        val currentPassword = etCurrentPassword.text.toString()
        val newPassword = etNewPassword.text.toString()

        return if (currentPassword.isNotEmpty() && newPassword.isNotEmpty()) {
            return if (currentPassword == accountEntity?.password) {
                true
            } else {
                showMessage(getString(R.string.error_wrong_password))
                false
            }
        } else if (currentPassword.isEmpty() && newPassword.isEmpty()) {
            true
        } else {
            showMessage(getString(R.string.error_empty_password))
            false
        }
    }
    //  выполняет валидацию полей имени и email. Ничего не принимает. Возвращает Boolean.
    private fun validateFields(): Boolean {
        hideSoftKeyboard()
        val allFields = arrayOf(etEmail, etName)
        var allValid = true
        for (field in allFields) {
            allValid = field.testValidity() && allValid
        }
        return allValid
    }
    //  присваивает переменной accountEntity данные аккаунта и заполняет поля имени, email и статуса.
    //  Выполняется при обновлении accountData в AccountViewModel. Принимает AccountEntity. Ничего не возвращает.
    private fun handleAccount(account: AccountEntity?) {
        hideProgress()
        accountEntity = account
        account?.let {
            GlideHelper.loadImage(requireActivity(), it.image, imgPhoto)
            etEmail.setText(it.email)
            etName.setText(it.name)
            etStatus.setText(it.status)

            etCurrentPassword.setText("")
            etNewPassword.setText("")
        }
    }

    //  запускает активити камеры. Выполняется при обновлении cameraFileCreated в MediaViewModel. Принимает Ничего не возвращает.
    private fun onCameraFileCreated(uri: Uri?) {
        base {
            if (uri != null) {
                navigator.showCamera(this, uri)
            }
        }
    }
    //  устанавливает новое изображение пользователя. Выполняется при обновлении pickedImageData в MediaViewModel.
    //  Принимает PickedImage. Ничего не возвращает.
    private fun onImagePicked(pickedImage: MediaViewModel.PickedImage?) {
        if (pickedImage != null) {
            accountEntity?.image = pickedImage.string

            val placeholder = imgPhoto.drawable
            Glide.with(this)
                .load(pickedImage.bitmap)
                .placeholder(placeholder)
                .error(placeholder)
                .into(imgPhoto)
        }
    }
    //  показывает сообщение о успешном обновлении аккаунта. Принимает Ничего не возвращает.
    private fun handleEditingAccount(account: AccountEntity?) {
        showMessage(getString(R.string.success_edit_user))
        accountViewModel.getAccount()
    }

    //  показывает или скрывает прогресс бар на изображении пользователя. Выполняется при обновлении progressData в BaseViewModel.
    //  Принимает Boolean. Ничего не возвращает.
    override fun updateProgress(progress: Boolean?) {   //  была добавлена в BaseFragment, что требует наличия модификатора override.
        if (progress == true) {
            groupProgress.visibility = View.VISIBLE
        } else {
            groupProgress.visibility = View.GONE
        }
    }
}