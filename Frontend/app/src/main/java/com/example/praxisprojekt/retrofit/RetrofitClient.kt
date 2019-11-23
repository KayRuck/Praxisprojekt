package com.example.praxisprojekt.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL_HOME = "192.168.0.185:5555"
    private const val BASE_URL = "http://localhost:5555"
    private const val BASE_URL2 = "http://10.0.2.2:5555"

    private val client = OkHttpClient().newBuilder().build()

    init {
        // TODO Mach das es Funktioniert
        // https://www.youtube.com/watch?v=OehA8wYCuZw


        val instance: RetroService by lazy {
            val retro = Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            retro.create(RetroService::class.java)
        }
    }
}

