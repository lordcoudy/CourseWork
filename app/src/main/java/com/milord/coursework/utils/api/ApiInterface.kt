package com.milord.coursework.utils.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.ArrayList

interface ApiInterface {
    @GET(Constants.USER_URL)
    fun getUser(@Header("Authorization") token: String): Call<UserHelper>

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST(Constants.REGISTER_URL)
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @POST(Constants.STORE_READINGS)
    fun storeReadings(@Header("Authorization") token: String, @Body request: ArrayList<StoreReadingsRequest>): Call<StoreReadingsResponse>

    @GET(Constants.BALANCE_URL)
    fun getBalance(@Header("Authorization") token: String): Call<BalanceResponse>

    @GET(Constants.LAST_READINGS)
    fun getLastReadings(@Header("Authorization") token: String): Call<StoreReadingsRequestArr>

    @GET(Constants.PAYMENTS)
    fun getPayments(@Header("Authorization") token: String): Call<PaymentExt>

    @POST(Constants.TOP_UP)
    fun topUp(@Header("Authorization") token: String, @Body request: TopUpRequest): Call<TopUpResponse>

}