package database

import data.Course
import database.DatabaseService.Companion.dbQuery
import org.jetbrains.exposed.sql.*


object CourseService {

    suspend fun getAllCourses(): List<Course> = dbQuery {
        Courses.selectAll().mapNotNull { toCourse(it) }
    }

    suspend fun getAllCoursesExecptFromUserBy(userID: Int): List<Course> = dbQuery {
        Courses.select { (Courses.fk_creator neq userID) }.mapNotNull { toCourse(it) }
    }

    suspend fun getAllCoursesFrom(userID: Int): List<Course> = dbQuery {
        Courses.select { (Courses.fk_creator eq userID) }.mapNotNull { toCourse(it) }
    }

    suspend fun getCourseByID(id: Int): Course? = dbQuery {
        Courses.select {
            (Courses.id eq id)
        }.mapNotNull { toCourse(it) }.singleOrNull()
    }

    suspend fun deleteCourse(id: Int): Boolean = dbQuery {
        LocToCourses.deleteWhere { (LocToCourses.fk_CourseID eq id) }
        Courses.deleteWhere { Courses.id eq id } > 0
    }

    suspend fun addCourse(course: Course): Course {
        var key: Int? = 0

        dbQuery {
            key = Courses.insert {
                it[title] = course.title
                it[description] = course.description
                it[state] = course.state
                it[cLocLat] = course.cLocLat
                it[cLocLang] = course.cLocLang
                it[privateUsage] = course.privateUsage
                it[inReturnValue] = course.inReturnValue
                it[fk_creator] = course.fk_creator
                it[fk_modules] = course.fk_modules
                it[fk_return] = course.fk_return
            } get Courses.id
        }
        return getCourseByID(key!!)!!
    }

    private fun toCourse(row: ResultRow): Course = Course(
        id = row[Courses.id],
        title = row[Courses.title],
        description = row[Courses.description],
        state = row[Courses.state],
        cLocLang = row[Courses.cLocLang],
        cLocLat = row[Courses.cLocLat],
        privateUsage = row[Courses.privateUsage],
        inReturnValue = row[Courses.inReturnValue],

        fk_creator = row[Courses.fk_creator],
        fk_return = row[Courses.fk_return],
        fk_modules = row[Courses.fk_modules]
    )

    suspend fun getCourseByUserID(id: Int): Course? = dbQuery {
        Courses.select {
            (Courses.fk_creator eq id)
        }.mapNotNull { toCourse(it) }.singleOrNull()
    }

    suspend fun updateCourse(course: Course, id: Int): Course {
        dbQuery {
            Courses.update({ Courses.id eq id }) {
                it[title] = course.title
                it[description] = course.description
                it[state] = course.state
                it[cLocLat] = course.cLocLat
                it[cLocLang] = course.cLocLang
                it[privateUsage] = course.privateUsage
                it[inReturnValue] = course.inReturnValue
                it[fk_creator] = course.fk_creator
                it[fk_modules] = course.fk_modules
                it[fk_return] = course.fk_return
            }
        }
        return getCourseByID(id)!!
    }

}

