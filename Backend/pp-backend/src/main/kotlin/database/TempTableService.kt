package database

import data.Mod
import data.UserToModule
import database.DatabaseService.Companion.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object TempTableService {
    val query = """
            SELECT module            
            FROM Users u, Module m, UserToModules utm
            WHERE u.id = utm.userID
                AND m.id = utm.moduleID
        """

    suspend fun getModuleByUser(userID: Int): List<String> = dbQuery {
        (Modules innerJoin UserToModules innerJoin Users)
            .select {
//                (Users.id eq UserToModules.fk_UserID) and (Modules.id eq UserToModules.fk_ModuleID) and
                        (Users.id eq userID)
            }.mapNotNull { mapToString(it) }
    }

    suspend fun getAllUserModulesBy(userID: Int) = UserToModules.select {
        (UserToModules.fk_UserID eq userID)
    }.mapNotNull { }

    suspend fun bindModuleToUser(userID: Int, modID: Int): List<String> {
        dbQuery {
            UserToModules.insert {
                it[fk_UserID] = userID
                it[fk_ModuleID] = modID
            }
        }
        return getModuleByUser(userID)
    }

    private fun mapModule(row: ResultRow): Mod = Mod(
        id = row[Modules.id],
        title = row[Modules.title],
        description = row[Modules.description]
    )

    private fun mapToString(row: ResultRow): String? = row[Modules.title]

    suspend fun addModulesToUser(userToModule: UserToModule) {
        dbQuery {
            UserToModules.insert {
                it[fk_UserID] = userToModule.fk_UserID
                it[fk_ModuleID] = userToModule.fk_modID
            }
        }
    }
}




