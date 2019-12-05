package com.example.praxisprojekt.retrofit

import com.google.gson.annotations.SerializedName

data class RetroUser (

    @SerializedName("id") val id : Int? = null,
    @SerializedName("username") val username : String,
    @SerializedName("description") val description: String,
    @SerializedName("password") val password : String,
    @SerializedName("email") val email : String,
    @SerializedName("contact") val contact : String,
    @SerializedName("loc_lat") val loc_lat : Double,
    @SerializedName("loc_lang") val loc_lang : Double
)

data class RetroCourse (

    @SerializedName("id") val id : Int? = null,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("state") val state : Boolean,
    @SerializedName("cLocLat") val cLocLat : Double,
    @SerializedName("cLocLang") val cLocLang : Double,
    @SerializedName("privateUsage") val privateUsage : Boolean,
    @SerializedName("fk_creator") val fk_creator : Int,
    @SerializedName("fk_return") val fk_return : Int,
    @SerializedName("fk_modules") val fk_modules : Int
)