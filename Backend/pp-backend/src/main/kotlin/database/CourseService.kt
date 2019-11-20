package database

import data.Course
import data.Mod
import data.User
import database.DatabaseService.Companion.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

object CourseService {



    suspend fun getAllCourses() : List<Course> = DatabaseService.dbQuery {
        Courses.selectAll().mapNotNull { toCourse(it) }
    }

    private suspend fun getCourseByID(id: Int) : Course? = dbQuery{
        Courses.select {
            (Courses.id eq id)
        }.mapNotNull { toCourse(it) }.singleOrNull()
    }

    suspend fun addCourse(course: Course, creator : Int, module : Int, inReturn : Int ): Course {
        var key: Int? = 0

        dbQuery {
            key = Courses.insert {
                it[title] = course.title
                it[description] = course.description
                it[state] = course.state
                it[cLocLat] = course.cLocLat
                it[cLocLang] = course.cLocLang
                it[privateUsage] = course.privateUsage
                it[fk_creator] = creator
                it[fk_modules] = module
                it[fk_return] = inReturn
            } get Courses.id
        }
        return getCourseByID(key!!)!!
    }


    private fun toCourse(row: ResultRow) : Course = Course(
        id = row[Courses.id],
        title = row[Courses.title],
        description = row[Courses.description],
        state = row[Courses.state],
        cLocLang = row[Courses.cLocLang],
        cLocLat = row[Courses.cLocLat],
        privateUsage = row[Courses.privateUsage],

        fk_creator = row[Courses.fk_creator],
        fk_return =  row[Courses.fk_return],
        fk_modules = row[Courses.fk_modules]
    )

}

