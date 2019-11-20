package com.example.praxisprojekt.retrofit

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.PUT

interface RetroService {

    @GET("/users")
    fun getAllUser(): Call

    @PUT("/users")
    fun updateUsers(): Call

    @GET("/courses")
    fun getAllCourses(): Call


}