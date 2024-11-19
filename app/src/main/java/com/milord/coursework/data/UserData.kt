package com.milord.coursework.data

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class UserHelper(
    @SerializedName("id")
    var id : Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
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

    @SerializedName("address")
    var address: String,

    @SerializedName("payer_code")
    var INN: String
)

data class LoginResponse (
    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("access_token")
    var authToken: String,

    @SerializedName("user")
    var user: UserHelper
)

data class RegisterResponse (
    @SerializedName("access_token")
    var authToken: String,

    @SerializedName("token_type")
    var tokenType: String
)

class UserData (private var email: String, private var password: String)
{
    private var userInfo : UserHelper = UserHelper(0, "name", email, password, "", "")
    private var balance : BalanceData = BalanceData()
    private var dates: PaymentsDates = PaymentsDates()
    private var token : String = ""
    private val payments : ArrayList<Payment> = ArrayList()

    constructor(email: String, password: String, INN: String, address: String, name: String) : this(email, password)
    {
        userInfo.name = name
        userInfo.INN = INN
        userInfo.address = address
    }

    fun getName(): String
    {
        return userInfo.name
    }

    fun getEmail(): String
    {
        return userInfo.email
    }

    fun getAddress(): String
    {
        return userInfo.address
    }

    fun getINN(): String
    {
        return userInfo.INN
    }

    fun getPassword(): String
    {
    return userInfo.password
    }

    fun getBalance(): BalanceData
    {
        return balance
    }

    fun getDates(): PaymentsDates
    {
        return dates
    }

    fun getToken(): String
    {
        return token
    }

    fun getPayments(): ArrayList<Payment>
    {
        return payments
    }

    fun setName(name: String)
    {
        userInfo.name = name
    }

    fun setEmail(email: String)
    {
        userInfo.email = email
    }

    fun setPassword(password: String)
    {
        this.password = password
    }

    fun setAddress(address: String)
    {
        userInfo.address = address
    }

    fun setINN(INN: String)
    {
        userInfo.INN = INN
    }

    fun setBalance(balance: BalanceData)
    {
        this.balance = balance
    }

    fun setDates(dates: PaymentsDates)
    {
        this.dates = dates
    }

    fun setToken(token: String)
    {
        this.token = token
    }

    fun addPayment(payment: Payment)
    {
        this.payments.add(payment)
    }
}