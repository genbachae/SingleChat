package ru.genbach.chat.remote.service

import ru.genbach.chat.remote.account.AuthResponse
import ru.genbach.chat.remote.core.BaseResponse
import ru.genbach.chat.remote.friends.GetFriendRequestsResponse
import ru.genbach.chat.remote.friends.GetFriendsResponse
import ru.genbach.chat.remote.messages.GetMessagesResponse
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
//  Интерфейс с функциями API. Содержит: вспомогательные константы с именем метода и параметрами, функцию для регистрации(fun register(…)).
//  Для формирования API запросов.

interface ApiService {
    companion object {
        //methods
        const val REGISTER = "register.php"
        const val LOGIN = "login.php"                                               //	4 урок: добавлена вспомогательная константа
        const val UPDATE_TOKEN = "updateUserToken.php"                              //	4 урок: добавлена вспомогательная константа
        const val ADD_FRIEND = "addFriend.php"                                      //	5 урок: добавлена вспомогательная константа
        const val APPROVE_FRIEND_REQUEST = "approveFriendRequest.php"               //	5 урок: добавлена вспомогательная константа
        const val CANCEL_FRIEND_REQUEST = "cancelFriendRequest.php"                 //	5 урок: добавлена вспомогательная константа
        const val DELETE_FRIEND = "deleteFriend.php"                                //	5 урок: добавлена вспомогательная константа
        const val GET_FRIENDS = "getContactsByUser.php"                             //	5 урок: добавлена вспомогательная константа
        const val GET_FRIEND_REQUESTS = "getFriendRequestsByUser.php"               //	5 урок: добавлена вспомогательная константа
        const val EDIT_USER = "editUser.php"                                        //	6 урок: добавлена вспомогательная константа
        const val SEND_MESSAGE = "sendMessage.php"
        const val GET_LAST_MESSAGES = "getLastMessagesByUser.php"
        const val GET_MESSAGES_WITH_CONTACT = "getMessagesByUserWithContact.php"

        //params
        const val PARAM_EMAIL = "email"
        const val PARAM_PASSWORD = "password"
        const val PARAM_NAME = "name"
        const val PARAM_TOKEN = "token"
        const val PARAM_USER_DATE = "user_date"
        const val PARAM_USER_ID = "user_id"                  //	4 урок: добавлена вспомогательная константа
        const val PARAM_OLD_TOKEN = "old_token"              //	4 урок: добавлена вспомогательная константа
        const val PARAM_REQUEST_USER_ID = "request_user_id"  //	5 урок: добавлена вспомогательная константа
        const val PARAM_FRIENDS_ID = "friends_id"            //	5 урок: добавлена вспомогательная константа
        const val PARAM_STATUS = "status"                    //	5 урок: добавлена вспомогательная константа
        const val PARAM_REQUEST_USER = "request_user"        //	5 урок: добавлена вспомогательная константа
        const val PARAM_APPROVED_USER = "approved_user"      //	5 урок: добавлена вспомогательная константа
        const val PARAM_IMAGE_NEW = "image_new"              //	6 урок: добавлена вспомогательная константа
        const val PARAM_IMAGE_NEW_NAME = "image_new_name"    //	6 урок: добавлена вспомогательная константа
        const val PARAM_IMAGE_UPLOADED = "image_uploaded"    //	6 урок: добавлена вспомогательная константа
        const val PARAM_IMAGE = "image"                      //	6 урок: добавлена вспомогательная константа

        const val PARAM_SENDER_ID = "sender_id"         //	9 урок: добавлена вспомогательная константа PARAM_SENDER_ID
        const val PARAM_RECEIVER_ID = "receiver_id"	    //	9 урок: добавлена вспомогательная константа PARAM_RECEIVER_ID
        const val PARAM_MESSAGE = "message"	            //	9 урок: добавлена вспомогательная константа PARAM_MESSAGE
        const val PARAM_MESSAGE_TYPE = "message_type_id"//	9 урок: добавлена вспомогательная константа PARAM_MESSAGE_TYPE
        const val PARAM_MESSAGE_DATE = "message_date"	//	9 урок: добавлена вспомогательная константа PARAM_MESSAGE_DATE
        const val PARAM_CONTACT_ID = "contact_id"	    //	9 урок: добавлена вспомогательная константа PARAM_CONTACT_ID

        const val PARAM_SENDER_USER = "senderUser"  	//	9 урок: добавлена вспомогательная константа PARAM_SENDER_USER
        const val PARAM_SENDER_USER_ID = "senderUserId"	//	9 урок: добавлена вспомогательная константа PARAM_SENDER_USER_ID
        const val PARAM_RECEIVED_USER_ID = "receivedUserId"	//	9 урок: добавлена вспомогательная константа PARAM_RECEIVED_USER_ID
        const val PARAM_MESSAGE_ID = "message_id"	    //	9 урок: добавлена вспомогательная константа PARAM_MESSAGE_ID
    }

    @FormUrlEncoded
    @POST(REGISTER)
    //  формирует API запрос для регистрации. Использует аннотации @FormUrlEncoded и @POST(<Имя метода API>).
    //  Принимает Map<String, String>. Возвращает Call<BaseResponse>.
    fun register(@FieldMap params: Map<String, String>): Call<BaseResponse>

    @FormUrlEncoded
    @POST(LOGIN)    //  формирует API запрос для авторизации. Принимает Map<String, String>. Возвращает Call<AuthResponse>.
    fun login(@FieldMap params: Map<String, String>): Call<AuthResponse>

    @FormUrlEncoded
    @POST(UPDATE_TOKEN) //  формирует API запрос для обновления токена. Принимает Map<String, String>. Возвращает Call<BaseResponse>.
    fun updateToken(@FieldMap params: Map<String, String>): Call<BaseResponse>

    @FormUrlEncoded
    @POST(ADD_FRIEND)
    //  формирует API запрос для оправки приглашения дружбы. Принимает Map<String, String>. Возвращает Call<BaseResponse>.
    fun addFriend(@FieldMap params: Map<String, String>): Call<BaseResponse>

    @FormUrlEncoded
    @POST(APPROVE_FRIEND_REQUEST)
    //  формирует API запрос для принятия приглашения дружбы. Принимает Map<String, String>. Возвращает Call<BaseResponse>.
    fun approveFriendRequest(@FieldMap params: Map<String, String>): Call<BaseResponse>

    @FormUrlEncoded
    @POST(CANCEL_FRIEND_REQUEST)
    //  формирует API запрос для отклонения приглашения дружбы. Принимает Map<String, String>. Возвращает Call<BaseResponse>.
    fun cancelFriendRequest(@FieldMap params: Map<String, String>): Call<BaseResponse>

    @FormUrlEncoded
    @POST(DELETE_FRIEND)
    //  формирует API запрос для удаления друга. Принимает Map<String, String>. Возвращает Call<BaseResponse>.
    fun deleteFriend(@FieldMap params: Map<String, String>): Call<BaseResponse>

    @FormUrlEncoded
    @POST(GET_FRIENDS)
    //  формирует API запрос для получения списка друзей. Принимает Map<String, String>. Возвращает Call<GetFriendsReponse>.
    fun getFriends(@FieldMap params: Map<String, String>): Call<GetFriendsResponse>

    @FormUrlEncoded
    @POST(GET_FRIEND_REQUESTS)
    //  формирует API запрос для получения списка входящих приглашений дружбы. Принимает Map<String, String>.
    //  Возвращает Call<GetFriendRequestsReponse>.
    fun getFriendRequests(@FieldMap params: Map<String, String>): Call<GetFriendRequestsResponse>

    @FormUrlEncoded
    @POST(EDIT_USER)
    fun editUser(@FieldMap params: Map<String, String>): Call<AuthResponse>


    @FormUrlEncoded
    @POST(SEND_MESSAGE) 	//	9 урок: добавлена вспомогательная константа SEND_MESSAGE
    //  формирует API запрос для оправления сообщения. Принимает Map<String, String>. Возвращает Call<BaseResponse>.
    fun sendMessage(@FieldMap params: Map<String, String>): Call<BaseResponse>

    @FormUrlEncoded
    @POST(GET_LAST_MESSAGES)    //	9 урок: добавлена вспомогательная константа GET_LAST_MESSAGES
    //  формирует API запрос для получения списка чатов. Принимает Map<String, String>. Возвращает Call<GetMessagesResponse>.
    fun getLastMessages(@FieldMap params: Map<String, String>): Call<GetMessagesResponse>

    @FormUrlEncoded
    @POST(GET_MESSAGES_WITH_CONTACT)	//	9 урок: добавлена вспомогательная константа GET_MESSAGES_WITH_CONTACT
    //  формирует API запрос для получения списка сообщений с контактом. Принимает Map<String, String>. Возвращает Call<GetMessagesResponse>.
    fun getMessagesWithContact(@FieldMap params: Map<String, String>): Call<GetMessagesResponse>
}