package com.example.praxisprojekt.retrofit

import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("id") val id : Int,
    @SerializedName("username") val username : String,
    @SerializedName("password") val password : String,
    @SerializedName("email") val email : String,
    @SerializedName("contact") val contact : String,
    @SerializedName("loc_lat") val loc_lat : Int,
    @SerializedName("loc_lang") val loc_lang : Int
)