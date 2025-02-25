package database

import org.jetbrains.exposed.sql.Table

/**
 * Data Structure for the MySql database
 * */

object Users : Table("Users") {
    val id = integer("userID").autoIncrement("seq_user").primaryKey()
    val username = varchar("username", 255)
    val description = varchar("description", 255)
    val password = varchar("password", 255)
    val email = varchar("email", 255)
    val contact = varchar("contact_private", 15).nullable()
    val loc_lat = double("Location_lat")
    val loc_lang = double("Location_long")
}

object Modules : Table("Modules") {
    val id = integer("moduleID").autoIncrement().primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

// Teaching Locations - Ort der Nachhilfe
object TeachLocs : Table("TeachLocs") {
    val id = integer("teachLocID").autoIncrement().primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

// In Return/Consideration - Gegenleistung
object InReturns : Table("InReturns") {
    val id = integer("returnID").autoIncrement().primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

object Courses : Table("Courses") {
    val id = integer("courseID").autoIncrement("seq_cou").primaryKey()
    val title = varchar("title", 255)
    var description = varchar("description", 255)
    val state = bool("activity state")
    val cLocLat = double("latitude")
    val cLocLang = double("longitude")
    val privateUsage = bool("private usage")
    val inReturnValue = integer("value for InReturn Money")

    val fk_creator = reference("creatorID", Users.id)
    val fk_return = reference("returnID", InReturns.id)
    val fk_modules = reference("moduleID", Modules.id)
}

/*Zwischen Tabellen*/
object LocToCourses : Table("LocToCourses"){
    val fk_TeachLocID = reference("teachLocID", TeachLocs.id).primaryKey(0)
    val fk_CourseID = reference("courseID", Courses.id).primaryKey(1)
}

object UserToModules : Table("UserToModules"){
    val fk_UserID = reference("userID", Users.id).primaryKey(0)
    val fk_ModuleID = reference("moduleID", Modules.id).primaryKey(1)
}
