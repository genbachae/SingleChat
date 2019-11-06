package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import javax.inject.Inject

class EditAccount @Inject constructor(          //  Для изменения данных текущего пользователя.
    private val repository: AccountRepository   //  объект репозитория AccountRepository.
) : UseCase<AccountEntity, AccountEntity>() {   //  Наследуется от: UseCase с параметризированными типами AccountEntity и AccountEntity.
    //  обращается к репозиторию для изменения данных пользователя. Принимает AccountEntity. Возвращает Either<Failure, AccountEntity>.
    override suspend fun run(params: AccountEntity): Either<Failure, AccountEntity> {
        return repository.editAccount(params)
    }
}