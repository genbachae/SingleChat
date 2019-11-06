package ru.genbach.chat.data.friends

import ru.genbach.chat.domain.friends.FriendEntity

interface FriendsCache {    //  Интерфейс. Содержит функции для взаимодействия с друзьями в базе данных.
    fun saveFriend(entity: FriendEntity)            //  сохраняет друга в бд. Принимает FriendEntity. Ничего не возвращает.

    fun getFriend(key: Long): FriendEntity?         //  получает друга из бд по идентификатору. Принимает Long: key. Возвращает FriendEntity.

    fun getFriends(): List<FriendEntity>            //  получает список друзей текущего пользователя. Ничего не принимает. Возвращает List<FriendEntity>.

    fun getFriendRequests(): List<FriendEntity>     //  получает список входящих приглашений на добавление в друзья. Ничего не принимает. Возвращает List<FriendEntity>.

    fun removeFriendEntity(key: Long)               //  удаляет друга из бд по идентификатору. Принимает Long: key. Ничего не возвращает.
}