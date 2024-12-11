package com.milord.coursework.utils.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    const val LOGIN_URL = "login"
    const val REGISTER_URL = "register"
    const val USER_URL = "user"
    const val BALANCE_URL = "balance"
    const val STORE_READINGS = "storereadings"
    const val LAST_READINGS = "lastreadings"
    const val PAYMENTS = "operationshistory"
    const val TOP_UP = "topupbalance"
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
