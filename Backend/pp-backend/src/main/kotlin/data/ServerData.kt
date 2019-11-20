package data

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

data class Mod(
    val id: Int,
    val title: String,
    val description: String
)

// Teaching Location - Ort der Nachhilfe
data class TeachLoc(
    val id: Int,
    val title: String,
    val description: String
)

data class InReturn(
    val id: Int,
    val title: String,
    val description: String
)

data class Course(
    val id: Int,
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

/*Zwischen Tabellen*/
data class LocToCourse(
    val fk_teachLocID: TeachLoc,
    val fk_courseID: Course
)

data class UserToModule(
    val fk_UserID: User,
    val fk_modID: Mod
)

data class UserToCourse(
    val fk_UserID: User,
    val fk_courseID: Course
)

