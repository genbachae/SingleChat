package ru.genbach.chat.domain.messages

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "messages_table")       //  принадлежность к таблице message_table базы данных Room.
@TypeConverters(ContactConverter::class)    //  конвертер типа, преобразовывающий Contact в String для хранения в бд.

data class MessageEntity(           //  Модельный класс. Содержит данные сообщения.
    @PrimaryKey
    @SerializedName("message_id")
    @ColumnInfo(name = "message_id")
    var id: Long,                           //  id сообщения. Тип: Long.
    @SerializedName("sender_id")
    @ColumnInfo(name = "sender_id")
    var senderId: Long,                     //  id отправителя сообщения. Тип: Long.
    @SerializedName("receiver_id")
    @ColumnInfo(name = "receiver_id")
    var receiverId: Long,                   //   id получателя сообщения. Тип: Long.
    var message: String,                    //  текст сообщения. Тип: String.
    @SerializedName("message_date")
    @ColumnInfo(name = "message_date")
    var date: Long,                         //  дата. Тип: Long.
    @SerializedName("message_type_id")
    @ColumnInfo(name = "message_type_id")
    var type: Int,                          //  тип сообщения(1 – текст, 2 – изображение). Тип: Int.
    var contact: ContactEntity? = null,     //  данные контакта. Тип: Contact.
    var fromMe: Boolean = false             //  определяет, отправлено ли сообщение с текущего аккаунта. Тип: Boolean.
) {
    constructor() : this(0L,0L,0L,"",0L,0,null, false)
}

data class ContactEntity(           //  Модельный класс. Содержит данные контакта сообщения.
    @SerializedName("user_id")
    var id: Long,                   //  id контакта. Тип: Long.
    var name: String,               //  имя контакта. Тип: String.
    var image: String               //  фото контакта. Тип: String.
)

class ContactConverter {            //  Конвертер типа. Для преобразования Contact в String.
    @TypeConverter
    //   преобразовывает Contact в String с помощью конкатенации и разделителя «||». Принимает Contact. Возвращает String.
    fun toString(contact: ContactEntity?): String? {
        return if (contact == null) null else "${contact.id}||${contact.name}||${contact.image}"
    }

    @TypeConverter
    //  восстанавливает Contact из String с помощью разделения строки. Принимает String. Возвращает Contact.
    fun toContact(string: String?): ContactEntity? {
        return if (string == null) {
            null
        } else {
            val arr = string.split("||")
            ContactEntity(arr[0].toLong(), arr[1], arr[2])
        }
    }
}
