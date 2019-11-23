package com.example.praxisprojekt.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT

interface RetroService {

    @GET("users")
    fun getAllUser(): Call<RetroUser>

    @GET("/users/{id}")
    fun getUserById(): Call<RetroUser>

    @PUT("/users")
    fun updateUsers(): Call<RetroUser>

    @GET("/courses")
    fun getAllCourses(): Call<List<RetroCourse>>


}

/**
 * Beispiel
 * interface WeatherService {
 * @GET("data/2.5/weather?")
 * fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String): Call<WeatherResponse>
}
 *
 */
