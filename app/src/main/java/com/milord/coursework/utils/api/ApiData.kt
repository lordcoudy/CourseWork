package com.milord.coursework.utils.api

import com.google.gson.annotations.SerializedName

enum class ReadingType {
    UNKNOWN,
    ELECTRICITY,
    COLD_WATER,
    HOT_WATER,
    HOUSE_HEATING,
    MAINTENANCE
}

data class UserHelper(
    @SerializedName("message")
    var message: String,
    @SerializedName("id")
    var id : Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("payer_code")
    var INN: String
)

data class LoginRequest (
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)

data class RegisterRequest (
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("password_confirmation")
    var passwordConfirmation: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("payer_code")
    var INN: String
)

data class AuthResponse (
    @SerializedName("message")
    var message: String,
    @SerializedName("access_token")
    var authToken: String,
    @SerializedName("token_type")
    var tokenType: String
)

data class StoreReadingsRequest(
    @SerializedName("reading_type")
    var type: Int,
    @SerializedName("reading_value")
    var value: Double
)

data class GetReadingsRequestArr(
    @SerializedName("value")
    var readings: ArrayList<StoreReadingsRequest>
)

data class BalanceResponse (
    @SerializedName("message")
    var message: String,
    @SerializedName("value")
    var balance: String
)

data class TopUpRequest (
    @SerializedName("value")
    var amount: Double
)

data class Payment(
    @SerializedName("DateTime")
    val date: String,
    @SerializedName("Description")
    val description: String
)

data class PaymentExt(
    @SerializedName("value")
    var payments : ArrayList<Payment>
)
