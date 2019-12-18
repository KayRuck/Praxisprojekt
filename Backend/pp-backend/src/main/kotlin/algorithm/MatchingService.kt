package algorithm

import data.Course
import data.Mod
import database.CourseService
import database.TempTableService

object MatchingService {

    /**
     * @param courses Alle Kurse die nicht zu dem Nutzer gehören
     * @param currentMod Modulliste des aktuellen Nutzers
     */

    suspend fun findMatch(userID : Int): MutableList<Course> {
        // inti courses mit allen in der DB vorhandenen Kurse ohne die des Users
        var courses = mutableListOf<Course>()
        courses = CourseService.getAllCoursesExecptFromUserBy(userID).toMutableList()

        // inti currentMods mit den Modules des Users
        var currentMods = mutableListOf<Mod>()
        currentMods = TempTableService.getModuleBy(userID).toMutableList()

        println("Module Liste $currentMods")

        // Matching von der Liste mit den Modulen
        val toDelete = mutableListOf<Course>()
        courses.forEach { course ->
            if (!currentMods.any {it.id == course.fk_modules })
                toDelete.add(course)
        }
        courses.removeAll(toDelete)

        return courses
    }


    /*
    suspend fun findMatch(currentModules: List<Mod>): MutableList<Course> {
        // zu matchende Liste für den objectiveUser an Kursen
        var courses = mutableListOf<Course>()

        // init courses mit allen Kursen aus der DB
        courses = CourseService.getAllCourses().toMutableList()

        // lösche alle Kurse aus courses, welche nicht in currentModules vorhanden sind
        val toDelete = mutableListOf<Course>()
        courses.forEach { course ->
            if (!currentModules.any {it.id == course.fk_modules })
                toDelete.add(course)
        }
        courses.removeAll(toDelete)

        return courses
    }
    */

}
