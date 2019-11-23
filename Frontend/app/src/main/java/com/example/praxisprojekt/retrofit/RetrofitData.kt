package com.example.praxisprojekt.retrofit

import com.google.gson.annotations.SerializedName

data class RetroUser (

    @SerializedName("id") val id : Int,
    @SerializedName("username") val username : String,
    @SerializedName("password") val password : String,
    @SerializedName("email") val email : String,
    @SerializedName("contact") val contact : String,
    @SerializedName("loc_lat") val loc_lat : Int,
    @SerializedName("loc_lang") val loc_lang : Int
)

data class RetroCourse (

    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("state") val state : Boolean,
    @SerializedName("cLocLat") val cLocLat : Int,
    @SerializedName("cLocLang") val cLocLang : Int,
    @SerializedName("privateUsage") val privateUsage : Boolean,
    @SerializedName("fk_creator") val fk_creator : Int,
    @SerializedName("fk_return") val fk_return : Int,
    @SerializedName("fk_modules") val fk_modules : Int
)