package com.example.praxisprojekt.retrofit

import retrofit2.Call
import retrofit2.http.*

interface RetroService {

    // String mit Hash Password und ID
    @GET("/login/{email}")
    fun getLoginData(@Query("email") email: String) : Call<RetroUser>

    @GET("/login/email/password")
    fun getLogin() : String

    @GET("/users/{id}")
    fun getUserById(@Query("id") id: Int): Call<RetroUser>

    @GET("/courses/{id}")
    fun getCourseById(@Query("id") id: Int): Call<RetroCourse>

    @GET("/courses")
    fun getAllCourses(): Call<List<RetroCourse>>

    @GET("/users/{id}/modules")
    fun getModulesFromUser(@Query("id") id : Int) : Call<List<String>>

    @GET("/course/{id}/location")
    fun getLocationFromCourse(@Query("id") id : Int) : Call<List<String>>

    @POST("/user/register")
    fun createUser(@Body retroUser: RetroUser): Call<RetroUser>

    @POST("/course/register")
    fun createCourse(@Body course: RetroCourse): Call<RetroCourse>

    @PUT("/user/{id}")
    fun updateUser(@Query("id") id : Int, @Body retroUser: RetroUser) : Call<RetroUser>

    @PUT("/course/{id}")
    fun updateCourse(@Query("id") id : Int, @Body retroCourse: RetroCourse) : Call<RetroCourse>

    @GET("/matching/{id}")
    fun getAllMatchedCourses(@Query("id") id: Int): Call<List<RetroCourse>>

    @GET("/users/{id}/courses")
    fun getAllUserCourses(@Query("id") id: Int): Call<List<RetroCourse>>

}

