package com.asiauniv.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyRetrofit {

    init {
        println("INIT")
    }

//    const val BASE_URL = "http://da8fd222.ngrok.io"
    const val BASE_URL = "http://10.0.2.2:5000"

    val instant: Retrofit by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit
    }

}
