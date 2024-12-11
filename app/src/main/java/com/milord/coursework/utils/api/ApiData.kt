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

data class StoreReadingsResponse (
    @SerializedName("message")
    var status: String
)

data class BalanceResponse (
    @SerializedName("")
    var balance: String
)

data class TopUpRequest (
    @SerializedName("amount")
    var amount: Double
)

data class TopUpResponse (
    @SerializedName("message")
    var status: String
)
