package database

import org.jetbrains.exposed.sql.Table



/**
 * Data Structure for the MySql database
 * */
// TODO: Fehler bei den Foreign Key ausbessern


object Users : Table("Users") {
    val id = integer("person_ID").autoIncrement("seq_user").primaryKey()
    val username = varchar("username", 25)
    val password = varchar("password", 25)
    val email = varchar("email", 255)
    val contact = varchar("contact_private", 15).nullable()
    val loc_lat = double("Location_lat")
    val loc_lang = double("Location_long")
}

object Modules : Table("Modules") {
    val id = integer("moduleID").autoIncrement("seq_mod").primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

// Teaching Locations - Ort der Nachhilfe
object TeachLocs : Table("TeachLocs") {
    val id = integer("teachLocID").autoIncrement("seq_teaLoc").primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

// In Return/Consideration - Gegenleistung
object InReturns : Table("InReturns") {
    val id = integer("returnID").autoIncrement("seq_ret").primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

object Courses : Table("Courses") {
    val id = integer("courseID").autoIncrement("seq_cou").primaryKey()
    val title = varchar("title", 25)
    val description = varchar("description", 25)
    val students = integer("number of Students")
    val state = Boolean
    val ad_loc_lat = double("latitude")
    val ad_loc_lang = double("longitude")
    val privateUsage = Boolean

    val fk_creatorID = reference("creatorID", Users.id)
    val fk_returnID = reference("returnID", InReturns.id)
    val fk_modulesID = reference("moduleID", Modules.id)
}

/*Zwischen Tabellen*/
object LocToCourses : Table("LocToCourses"){
    val fk_teachLocID = reference("teachLocID", TeachLocs.id).primaryKey(0)
    val fk_courseID = reference("courseID", Courses.id).primaryKey(1)
}

object UserToModules : Table("UserToModules"){
    val fk_UserID = reference("userID", Users.id).primaryKey(0)
    val fk_moduleID = reference("moduleID", Modules.id).primaryKey(1)
}

object UserToCourses : Table("UserToCourses"){
    val fk_UserID = reference("userID", Users.id).primaryKey(0)
    val fk_courseID = reference("courseID", Courses.id).primaryKey(1)

}

