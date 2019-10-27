package database

import org.jetbrains.exposed.sql.Database

class DataAccess {

    init {
        val db = Database.Companion.connect(
            "jdbc:mysql://localhost:3306/praxisprojekt",
            driver = "com.mysql.jdbc.Driver",
            user = "root", password = "")

    }

}