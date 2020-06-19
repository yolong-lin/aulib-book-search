package com.asiauniv.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyRetrofit {

    const val BASE_URL = "https://aulib-api.herokuapp.com"

    val instant: Retrofit by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit
    }

}
