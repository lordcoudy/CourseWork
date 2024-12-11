package com.milord.coursework.utils.api

import com.milord.coursework.data.BalanceData
import com.milord.coursework.data.Payment
import com.milord.coursework.utils.api.LoginRequest
import com.milord.coursework.utils.api.AuthResponse
import com.milord.coursework.utils.api.RegisterRequest
import com.milord.coursework.utils.api.UserHelper
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
    fun getLastReadings(@Header("Authorization") token: String): Call<ArrayList<StoreReadingsRequest>>

    @GET(Constants.PAYMENTS)
    fun getPayments(@Header("Authorization") token: String): Call<ArrayList<Payment>>

    @POST(Constants.TOP_UP)
    fun topUp(@Header("Authorization") token: String, @Body request: TopUpRequest): Call<TopUpResponse>

}