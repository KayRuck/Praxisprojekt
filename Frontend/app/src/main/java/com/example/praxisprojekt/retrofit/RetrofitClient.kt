package com.example.praxisprojekt.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    // https://www.youtube.com/watch?v=OehA8wYCuZw
    // TODO Mach das es Funktioniert


    // private val client = OkHttpClient().newBuilder().build()

    fun getClient(baseURL: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}

