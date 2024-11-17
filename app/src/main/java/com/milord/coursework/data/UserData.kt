package com.milord.coursework.data

import android.location.Address

class UserData (private var email: String, private var password: String)
{
    private var address: String = ""
    private var INN: String = ""
    private var balance : BalanceData = BalanceData()
    private var dates: PaymentsDates = PaymentsDates()

    constructor(email: String, password: String, INN: String, address: String) : this(email, password)
    {
        this.INN = INN
        this.address = address
    }

    fun getEmail(): String
    {
        return email
    }

    fun getAddress(): String
    {
        return address
    }

    fun getINN(): String
    {
        return INN
    }

    fun setEmail(email: String)
    {
        this.email = email
    }

    fun setPassword(password: String)
    {
        this.password = password
    }

    fun getPassword(): String
    {
        return password
    }

    fun setAddress(address: String)
    {
        this.address = address
    }

    fun setINN(INN: String)
    {
        this.INN = INN
    }

    fun getBalance(): BalanceData
    {
        return balance
    }

    fun setBalance(balance: BalanceData)
    {
        this.balance = balance
    }

    fun getDates(): PaymentsDates
    {
        return dates
    }

    fun setDates(dates: PaymentsDates)
    {
        this.dates = dates
    }

    private fun isValid(): Boolean
    {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun isEmailValid(): Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(): Boolean
    {
        return password.length >= 6
    }

    fun isPasswordConfirmed(passwordConfirm: String): Boolean
    {
        return password == passwordConfirm
    }

    fun isDataValid(): Boolean
    {
        return isValid() && isEmailValid() && isPasswordValid()
    }
}