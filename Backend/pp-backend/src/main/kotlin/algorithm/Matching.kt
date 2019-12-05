package algorithm

import data.Course
import data.Mod
import data.User
import database.CourseService

class Matching {

    /**
     * Zwei Listen an.
     *
     * Eine enthält die Daten (ID und Fächer) des Clients
     * Mit der anderen wird durch alle im Server befindlichen Entitäten durchlaufen und die akutellen Daten gespeichter
     * über die ID werden die eigenen Anzeigen des Clients raus gefiltert
     *
     *
     *
     */


    /**
     * @param objectiveUser Aktueller Nutzer, welcher nach matches sucht
     * @param currentModules Modulliste des aktuellen Nutzers
     */
    suspend fun findMatch(objectiveUser: User, currentModules: List<Mod>): MutableList<Course> {
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


    // TODO Filtering Matches - Show only requested Modules
}

