package com.example.praxisprojekt.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    // TODO Mach das es Funktioniert
    // https://www.youtube.com/watch?v=OehA8wYCuZw
    private const val BASE_URL = "localhost/5555"

    private val client = OkHttpClient().newBuilder().build()

    val instance : RetroService by lazy{
        val retro = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retro.create(RetroService::class.java)
    }
}

