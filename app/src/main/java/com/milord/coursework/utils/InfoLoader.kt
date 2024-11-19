package com.milord.coursework.utils

import com.milord.coursework.data.BalanceData
import com.milord.coursework.data.PaymentsDates
import com.milord.coursework.data.UserData

// Класс-заглушка для загрузки данных о пользователе
class InfoLoader
{
    private lateinit var data: UserData
    private lateinit var balanceData: BalanceData
    private lateinit var paymentsDates: PaymentsDates

    fun loadData(mail : String, password: String)
    {
        data = UserData(mail, password, "0000000000", "Moscow", "Ivan Ivanov")
        balanceData = BalanceData(balance = 90.0, electricity = 100.0, coldWater = 100.0, hotWater = 100.0, cap = 100.0)
        data.setBalance(balanceData)
        paymentsDates = PaymentsDates(electricityDate = "01.01.2022", coldWaterDate = "01.01.2022", hotWaterDate = "01.01.2022")
        data.setDates(paymentsDates)
    }

    fun getData(): UserData
    {
        return data
    }
}