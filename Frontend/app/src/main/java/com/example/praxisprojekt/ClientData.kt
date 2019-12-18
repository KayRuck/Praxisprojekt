package com.example.praxisprojekt

/**
 *  Data Structure for Kotlin Server - Client
 * */
data class User(
    val id: Int?,
    val username: String,
    val description: String,
    val password: String,
    val email: String,
    val contact: String?,
    val loc_lat: Double,
    val loc_lang: Double
)

enum class Mods (val id: Int, var title : String, var desc : String?) {

    APMOD(1, "Algorithmen und Programmierung", "Informatik"),
    MATH1INFMOD (2, "Mathematik 1", "Informatik - TI, ITM, MI, AI"),
    MATH2INFMOD (3, "Mathematik 2", "Informatik - TI, ITM, MI, AI"),
    MATHINFMOD (4, "Mathematik", "Informatik - WI"),
    BWL1INFMOD (5, "Betriebswirtschaftslehre 1", "Informatik")
}

enum class TeachLocs (val id: Int, var title : String, var desc : String?){

    TEACH(1, "Beim Tutors", "Unterricht am Standort des Tutors"),
    STUD(2, "Beim Studenten", "Unterricht am Standort des Lehrers"),
    TH(3, "An der TH", "Unterricht am Standort des Lehrers"),
    ONLINE(4, "Online Beratung", "Unterricht über Skype, Whatsapp, etc.")
}

enum class InReturns (val id: Int, var title : String, var desc : String?){
    MONEY(1, "Bezahlung", ""),
    HELP(2, "Hilfe in anderen Fächern", ""),
    MENSA(3, "Ein Essen in der Mensa", ""),
    COFFEE(4, "Kaffee", "")
}

data class Course(
    val id: Int?,
    val title: String,
    val description: String,
    val state: Boolean,
    val cLocLat: Double,
    val cLocLang: Double,
    val privateUsage: Boolean,
    val inReturnValue: Int,

    val fk_creator: Int, //User
    val fk_return: Int,  //InReturn
    val fk_modules: Int  //Mod
)

enum class Constants(val string: String){
    API_BASE_URL("http://192.168.0.185:5555")
}