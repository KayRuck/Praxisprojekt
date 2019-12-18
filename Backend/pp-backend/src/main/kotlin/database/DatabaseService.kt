package database


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseService {

    init {
        Database.Companion.connect(
            "jdbc:mysql://localhost:3306/praxisprojekt",
            driver = "com.mysql.jdbc.Driver",
            user = "root", password = ""
        )

        creatTables()
    }

    private fun creatTables() {

        transaction {

            SchemaUtils.drop(Users, Modules, TeachLocs, InReturns, Courses, LocToCourses, UserToModules)
            SchemaUtils.create(Users, Modules, TeachLocs, InReturns, Courses, LocToCourses, UserToModules)
        }
    }

    /**
     * TODO: CODE KOMMENTIEREN
     * Tutorial
     * https://ryanharrison.co.uk/2018/04/14/kotlin-ktor-exposed-starter.html
     */

    companion object {
        suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
                transaction { block() }
        }
    }
}