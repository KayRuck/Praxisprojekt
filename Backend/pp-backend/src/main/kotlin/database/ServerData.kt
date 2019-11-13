package database


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

data class Module(
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
    val students: Int,
    val state: Boolean,
    val ad_loc_lat: Double,
    val ad_loc_lang: Double,
    val privateUsage: Boolean,

    val fk_creatorID: User,
    val fk_returnID: InReturn,
    val fk_modulesID: Module
)

/*Zwischen Tabellen*/
data class LocToCourse(
    val fk_teachLocID: TeachLoc,
    val fk_courseID: Course
)

data class UserToModule(
    val fk_UserID: User,
    val fk_moduleID: Module
)

data class UserToCourse(
    val fk_UserID: User,
    val fk_courseID: Course
)

