package database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseService {

    init {
        val db = Database.Companion.connect(
            "jdbc:mysql://localhost:3306/praxisprojekt",
            driver = "com.mysql.jdbc.Driver",
            user = "root", password = ""
        )

        creatTables()
    }

    private fun creatTables() {

        transaction {

            SchemaUtils.drop(Users, Modules, TeachLocs, InReturns, Courses, LocToCourses, UserToModules, UserToCourses)
            SchemaUtils.create(Users, Modules, TeachLocs, InReturns, Courses, LocToCourses, UserToModules, UserToCourses)
        }
    }

    //TODO: Wenn es mit User Funktioniert mit allen Entitäten die gebraucht werden machen
    //vvvvvvvvvv

    /**
     * User Part
     * */

    suspend fun getAllUsers() : List<User> = dbQuery {
        Users.selectAll().mapNotNull{ toUser(it) }
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
        return getUser(key!!)!!
    }

    suspend fun getUser(id: Int): User? = dbQuery {
        Users.select {
            (Users.id eq id)
        }.mapNotNull { toUser(it) }
            .singleOrNull()
    }

    suspend fun deleteUser(id: Int) : Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    /**
     * Helper Methode for the Kotlin - Database Class Mapping
     * */
    private fun toUser(row: ResultRow): User = User(
        id = row[Users.id],
        username = row[Users.username],
        email = row[Users.email],
        password = row[Users.password],
        contact = row[Users.contact],
        loc_lat = row[Users.loc_lat],
        loc_lang = row[Users.loc_lang]

    )

    /**
     * TODO: CODE KOMMENTIEREN
     * Tutorial
     * https://ryanharrison.co.uk/2018/04/14/kotlin-ktor-exposed-starter.html
     */

    private suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }



}