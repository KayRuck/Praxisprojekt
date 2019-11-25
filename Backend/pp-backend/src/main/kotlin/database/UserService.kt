package database

import data.User
import database.DatabaseService.Companion.dbQuery
import org.jetbrains.exposed.sql.*

object UserService {

    suspend fun getAllUsers() : List<User> = dbQuery {
        Users.selectAll().mapNotNull{ toUser(it) }
    }

    suspend fun getUserByID(id: Int): User? = dbQuery {
        Users.select {
            (Users.id eq id)
        }.mapNotNull { toUser(it) }
            .singleOrNull()
    }

    suspend fun deleteUser(id: Int) : Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    suspend fun addUser(user: User): User {
        var key: Int? = 0

        dbQuery {
            key = Users.insert {
                it[username] = user.username
                it[email] = user.email
                it[password] = user.password
                it[contact] = user.contact
                it[loc_lat] = user.loc_lat
                it[loc_lang] = user.loc_lang
            } get Users.id
        }
        return getUserByID(key!!)!!
    }

    private fun toUser(row: ResultRow): User = User(
        id = row[Users.id],
        username = row[Users.username],
        email = row[Users.email],
        password = row[Users.password],
        contact = row[Users.contact],
        loc_lat = row[Users.loc_lat],
        loc_lang = row[Users.loc_lang]

    )



}