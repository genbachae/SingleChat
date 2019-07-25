package info.fandroid.chat.domain.account

import com.google.gson.annotations.SerializedName

class AccountEntity(
    @SerializedName("user_id")
    val id: Long,
    val name: String,
    val email: String,
    @SerializedName("token")
    val token: String,
    val status: String,
    @SerializedName("user_date")
    val userDate: Long,
    val image: String
)