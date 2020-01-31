package database

import data.LocToCourse
import data.Mod
import data.TeachLoc
import data.UserToModule
import database.DatabaseService.Companion.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object TempTableService {

    suspend fun getModuleByUser(userID: Int): List<String> = dbQuery {
        (Modules innerJoin UserToModules innerJoin Users)
            .select {
                (Users.id eq userID)
            }.mapNotNull { mapToModString(it) }
    }

    suspend fun getModuleBy(userID: Int): List<Mod> = dbQuery {
        (Modules innerJoin UserToModules innerJoin Users)
            .select {
                (Users.id eq userID)
            }.mapNotNull { mapModule(it) }
    }


    suspend fun getLocationByCourse(courseID: Int): List<String> = dbQuery {
        (TeachLocs innerJoin LocToCourses innerJoin Courses)
            .select {
            (Courses.id eq courseID)
        }.mapNotNull { mapToLocString(it) }
    }


    suspend fun getLocationBy(courseID: Int): List<TeachLoc> = dbQuery {
        (TeachLocs innerJoin LocToCourses innerJoin Courses)
            .select {
                (Courses.id eq courseID)
            }.mapNotNull { mapLocation(it) }
    }


    private fun mapModule(row: ResultRow): Mod = Mod(
        id = row[Modules.id],
        title = row[Modules.title],
        description = row[Modules.description]
    )

    private fun mapLocation(row: ResultRow): TeachLoc = TeachLoc(
        id = row[TeachLocs.id],
        title = row[TeachLocs.title],
        description = row[TeachLocs.description]
    )

    private fun mapToModString(row: ResultRow): String? = row[Modules.title]
    private fun mapToLocString(row: ResultRow): String? = row[TeachLocs.title]

    suspend fun addModulesToUser(userToModule: UserToModule) {
        dbQuery {
            UserToModules.insert {
                it[fk_UserID] = userToModule.fk_UserID
                it[fk_ModuleID] = userToModule.fk_ModID
            }
        }
    }

    suspend fun addLocationToCourse(locToCourse: LocToCourse) {
        dbQuery {
            LocToCourses.insert {
                it[fk_CourseID] = locToCourse.fk_CourseID
                it[fk_TeachLocID] = locToCourse.fk_TeachLocID
            }
        }
    }

    suspend fun deleteModules(id: Int) = dbQuery {
        UserToModules.deleteWhere { (UserToModules.fk_UserID eq id) }
    }

    suspend fun deleteLocations(id: Int) = dbQuery {
        LocToCourses.deleteWhere { (LocToCourses.fk_CourseID eq id) }
    }
}




