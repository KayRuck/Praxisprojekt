package com.example.praxisprojekt.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetroService {

    /*@GET("users")
    fun getAllUser(): Call<RetroUser>
    */

    @GET("/users/{id}")
    fun getUserById(@Query("id") id: Int): Call<RetroUser>

    @GET("/courses")
    fun getAllCourses(): Call<List<RetroCourse>>

    @POST("/user/register")
    fun createUser(@Body retroUser: RetroUser): Call<RetroUser>

    @POST("/course/create")
    fun createCourse(@Body course: RetroCourse): Call<RetroCourse>

}

/**
 * Beispiel
 * interface WeatherService {
 * @GET("data/2.5/weather?")
 * fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String): Call<WeatherResponse>
}
 *
 */
