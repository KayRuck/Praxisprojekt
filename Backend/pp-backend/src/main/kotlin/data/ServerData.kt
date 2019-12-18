package data

/**
 *  Data Structure for Kotlin Server - Client
 * */
data class User(
    val id: Int,
    val username: String,
    val description: String,
    val password: String,
    val email: String,
    val contact: String?,
    val loc_lat: Double,
    val loc_lang: Double
)

data class Mod(
    val id: Int,
    val title: String,
    val description: String?
)

// Teaching Location - Ort der Nachhilfe
data class TeachLoc(
    val id: Int,
    val title: String,
    val description: String?
)

data class InReturn(
    val id: Int,
    val title: String,
    val description: String?
)

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val state: Boolean,
    val cLocLat: Double,
    val cLocLang: Double,
    val privateUsage: Boolean,
    val inReturnValue: Int,

    val fk_creator: Int,
    val fk_return: Int,
    val fk_modules: Int
)

/*Zwischen Tabellen*/
data class LocToCourse(
    val fk_CourseID: Int,
    val fk_TeachLocID: Int
)

data class UserToModule(
    val fk_UserID: Int,
    val fk_ModID: Int
)

data class ModuleList(
    val modules: List<Int>
)

data class LocationList(
    val locations: List<Int>
)


