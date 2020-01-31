package database

import data.User
import database.DatabaseService.Companion.dbQuery
import org.jetbrains.exposed.sql.select


object LoginService {

    /**
     *
     * SELECT * FROM `users` WHERE users.email = "Kayayay.kartoffel@gmx.de"
     */


    suspend fun getEmailData(email: String): User? = dbQuery {
        Users.select {
            (Users.email eq email)
        }.mapNotNull { UserService.toUser(it) }
            .singleOrNull()

    }


}