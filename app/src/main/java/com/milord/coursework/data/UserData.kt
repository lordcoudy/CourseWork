package com.milord.coursework.data

import com.milord.coursework.utils.api.UserHelper
import java.util.ArrayList

class UserData (private var email: String)
{
    private var userInfo : UserHelper = UserHelper(0, "name", email, "", "")
    private var balance : BalanceData = BalanceData()
    private var token : String = ""
    private val payments : ArrayList<Payment> = ArrayList()

    constructor(email: String, INN: String, address: String, name: String) : this(email)
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

    fun getBalance(): BalanceData
    {
        return balance
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

    fun setToken(token: String)
    {
        this.token = token
    }

    fun addPayment(payment: Payment)
    {
        this.payments.add(payment)
    }

    fun setPayments(payments: ArrayList<Payment>)
    {
        this.payments.clear()
        this.payments.addAll(payments)
    }
}