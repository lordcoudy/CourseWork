package com.milord.coursework.data

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

    fun getApiService(): ApiInterface {

        // Initialize ApiService if not initialized yet
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
