package ru.genbach.chat.domain.friends

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "friends_table")
//  Модельный класс. Содержит данные друга.
data class FriendEntity(    //  Для сохранения в базу данных. Добавлена аннотация @Entity, маркирующая класс FriendEntity для сохранения в Room.
    @PrimaryKey             //  добавлена аннотация @PrimaryKey, назначающая поле id ключом для таблицы.
    @SerializedName("user_id")
    var id: Long,                           //  id друга. Тип: Long. 7й изменены на var (Mutable).
    var name: String,                       //  имя друга. Тип: String. 7й изменены на var (Mutable).
    var email: String,                      //  email друга. Тип: String. 7й изменены на var (Mutable).
    @ColumnInfo(name = "friends_id")        //  добавлена аннотация @ColumnInfo, присваивающая полям таблицы имена
    @SerializedName("friends_id")
    var friendsId: Long,                    //  id дружбы. Тип: Long. 7й изменены на var (Mutable).
    var status: String,                     //  статус друга. Тип: String. 7й изменены на var (Mutable).
    var image: String,                      //  фото друга. Тип: String. 7й изменены на var (Mutable).
    @ColumnInfo(name = "is_request")        //  добавлена аннотация @ColumnInfo, присваивающая полям таблицы имена
    var isRequest: Int = 0                  //  (по умолчанию используются имена полей класса) во избежание CamelCase’а в запросах.
    //  isRequest – переменная для хранения значения: является ли FriendEntity другом(= 0) или запросом в друзья(= 1). Тип: Int.
)