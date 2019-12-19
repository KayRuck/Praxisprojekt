package com.example.praxisprojekt.retrofit

import com.example.praxisprojekt.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val gson: Gson = GsonBuilder().setLenient().create()

    private fun getRetroClient() = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL.string)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getRetroService() : RetroService = getRetroClient().create(RetroService::class.java)

}

