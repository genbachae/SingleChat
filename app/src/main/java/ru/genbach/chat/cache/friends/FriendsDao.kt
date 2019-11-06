package ru.genbach.chat.cache.friends

import androidx.room.*
import ru.genbach.chat.data.friends.FriendsCache
import ru.genbach.chat.domain.friends.FriendEntity

@Dao
interface FriendsDao : FriendsCache {   //  Интерфейс-Dao. Для формирования SQL запросов.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(friendEntity: FriendEntity): Long

    @Update
    fun update(friendEntity: FriendEntity)

    @Transaction
    //  сохраняет друга в бд. Добавляет новое значение FriendEntity в таблицу (insert(…)).
    //  При наличии в таблице FriendEntity с таким же ключом, обновляет его (update(…)).
    //  Принимает FriendEntity. Ничего не возвращает.
    override fun saveFriend(entity: FriendEntity) {
        if (insert(entity) == -1L) {
            update(entity)
        }
    }

    @Query("SELECT * from friends_table WHERE id = :key")
    //  формирует запрос для получения друга из бд по идентификатору. Принимает Long: key. Возвращает FriendEntity.
    override fun getFriend(key: Long): FriendEntity?

    @Query("SELECT * from friends_table WHERE is_request = 0")
    //  формирует запрос для получения списка друзей текущего пользователя. Передает условие is_request = 0 (не является приглашением в друзья).
    //  Ничего не принимает. Возвращает List<FriendEntity>.
    override fun getFriends(): List<FriendEntity>

    @Query("SELECT * from friends_table WHERE is_request = 1")
    //  формирует запрос для получения список входящих приглашений на добавление в друзья.
    //  Передает условие is_request = 1 (является приглашением в друзья). Ничего не принимает. Возвращает List<FriendEntity>.
    override fun getFriendRequests(): List<FriendEntity>

    @Query("DELETE FROM friends_table WHERE id = :key")
    //  формирует запрос для удаления друга из бд по идентификатору. Принимает Long: key. Ничего не возвращает.
    override fun removeFriendEntity(key: Long)
}