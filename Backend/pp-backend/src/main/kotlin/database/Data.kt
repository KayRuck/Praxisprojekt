package database

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table



object User : Table("users") {
    val id = integer("userID").autoIncrement().primaryKey()
    val username = varchar("username", 25)
    val password = varchar("password", 25)
    val email = varchar("email", 255)
    val contact = varchar("contact", 15).nullable()
    val loc_lat = double("latitude")
    val loc_lang = double("longitude")
}

object Module : Table("module") {
    val id = text("moduleID").autoIncrement().primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

object TeachLoc : Table("teachingLocation") {
    val id = integer("teachLocID").autoIncrement().primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

object InReturn : Table("inReturn") {
    val id = integer("returnID").autoIncrement().primaryKey()
    val title = varchar("title", 255)
    val description = varchar("description", 255).nullable()
}

object Course : Table("Course") {
    val id = integer("courseID").autoIncrement().primaryKey()
    val title = varchar("title", 25)
    val description = varchar("description", 25)
    val students = integer("number of Students")
    val state = Boolean
    val ad_loc_lat = double("latitude")
    val ad_loc_lang = double("longitude")
    val privateUsage = Boolean

    val fk_creatorID = reference("creatorID", User.id)
    val fk_returnID = reference("returnID", InReturn.id)
    val fk_modulesID = reference("moduleID", Module.id)
}

object LocToCourse : Table("LocationToCourse"){
    val fk_teachLocID = reference("teachLocID", TeachLoc.id).primaryKey(0)
    val fk_courseID = reference("courseID", Course.id).primaryKey(1)
}

object UserToModule : Table("UserToModule"){
    val fk_UserID = reference("userID", User.id).primaryKey(0)
    val fk_moduleID = reference("moduleID", Module.id).primaryKey(1)
}

object UserToCourse : Table("UserToCourse"){
    val fk_UserID = reference("userID", User.id).primaryKey(0)
    val fk_courseID = reference("courseID", Course.id).primaryKey(1)

}


