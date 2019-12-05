package com.example.praxisprojekt.retrofit

import retrofit2.Call
import retrofit2.http.*

interface RetroService {

    /*@GET("users")
    fun getAllUser(): Call<RetroUser>
    */

    @GET("/users/{id}")
    fun getUserById(@Query("id") id: Int): Call<RetroUser>

    @GET("/courses")
    fun getAllCourses(): Call<List<RetroCourse>>

    @GET("/users/{id}/modules")
    fun getModulesFromUser(@Query("id") id : Int) : Call<List<String>>

    @POST("/user/register")
    fun createUser(@Body retroUser: RetroUser): Call<RetroUser>

    @POST("/course/register")
    fun createCourse(@Body course: RetroCourse): Call<RetroCourse>

    @PUT("/user/{id}")
    fun updateUser(@Query("id") id : Int, @Body retroUser: RetroUser) : Call<RetroUser>

}

/**
 * Beispiel
 * interface WeatherService {
 * @GET("data/2.5/weather?")
 * fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String): Call<WeatherResponse>
}
 *
 */
