package com.example.praxisprojekt.datastructure

/**
 *  Data Structure for Kotlin Server - Client
 * */
data class User(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val contact: String?,
    val loc_lat: Double,
    val loc_lang: Double
)

enum class Mods (val int: Int, var title : String, var desc : String?) {

    APMOD(0, "Algorithmen und Programmierung", "Informatik"),
    MATH1INFMOD (1, "Mathematik 1", "Informatik - TI, ITM, MI, AI"),
    MATH2INFMOD (2, "Mathematik 2", "Informatik - TI, ITM, MI, AI"),
    MATHINFMOD (3, "Mathematik", "Informatik - WI"),
    BWL1INFMOD (4, "Betriebswirtschaftslehre 1", "Informatik")
}

enum class TeachLocs (val int: Int, var title : String, var desc : String?){

    TEACH(0, "Beim Lehrer", ""),
    STUD(1, "Beim Studenten", "Informatik - TI, ITM, MI, AI"),
    TH(2, "An der TH", "Informatik - TI, ITM, MI, AI"),
    ONLINE(3, "Online Beratung", "Informatik - WI")
}

enum class InReturns (val int: Int, var title : String, var desc : String?){
    MONEY(0, "Bezahlung", ""),
    HELP(1, "Hilfe in anderen Fächern", ""),
    MENSA(2, "Ein Essen in der Mensa", ""),
    COFFEE(3, "Kaffee", "")
}

data class Course(
    val title: String,
    val description: String,
    val state: Boolean,
    val cLocLat: Double,
    val cLocLang: Double,
    val privateUsage: Boolean,

    val fk_creator: Int, //User
    val fk_return: Int,  //InReturn
    val fk_modules: Int  //Mod
)

/*Zwischen Tabellen
data class LocToCourse(
    val fk_teachLocID: TeachLocs,
    val fk_courseID: Course
)

data class UserToModule(
    val fk_UserID: User,
    val fk_modID: Mods
)

data class UserToCourse(
    val fk_UserID: User,
    val fk_courseID: Course
)

*/