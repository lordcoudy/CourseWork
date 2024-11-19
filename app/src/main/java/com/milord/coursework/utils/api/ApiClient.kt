package com.milord.coursework.utils.api

import com.milord.coursework.data.BalanceData
import com.milord.coursework.data.LoginRequest
import com.milord.coursework.data.LoginResponse
import com.milord.coursework.data.PaymentsDates
import com.milord.coursework.data.RegisterRequest
import com.milord.coursework.data.RegisterResponse
import com.milord.coursework.data.UserHelper
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

object Constants {
    const val LOGIN_URL = "login"
    const val REGISTER_URL = "register"
    const val USER_URL = "user"
    const val BALANCE_URL = "user/balance"
    const val DATES_URL = "user/dates"
    const val BASE_URL = "http://127.0.0.1:8000/api/"
}

class ApiClient {
    private lateinit var apiService: ApiInterface

    fun getApiService(): ApiInterface
    {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiInterface::class.java)
        }
        return apiService
    }
}
