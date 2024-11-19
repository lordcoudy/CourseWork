package com.milord.coursework.utils.api

import com.milord.coursework.data.BalanceData
import com.milord.coursework.data.LoginRequest
import com.milord.coursework.data.LoginResponse
import com.milord.coursework.data.PaymentsDates
import com.milord.coursework.data.RegisterRequest
import com.milord.coursework.data.RegisterResponse
import com.milord.coursework.data.UserHelper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET(Constants.USER_URL)
    fun getUser(): Call<UserHelper>

    @POST(Constants.LOGIN_URL)
    @FormUrlEncoded
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST(Constants.REGISTER_URL)
    @FormUrlEncoded
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET(Constants.BALANCE_URL)
    fun getBalance(): Call<BalanceData>

    @GET(Constants.DATES_URL)
    fun getDates(): Call<PaymentsDates>
}