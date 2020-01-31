import algorithm.MatchingService
import com.google.gson.Gson
import data.*
import database.*
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.util.pipeline.PipelineContext

/**
 * Server
 *
 * @author Kay Ruck
 */
class Server( databaseService: DatabaseService) {

    private val gson = Gson()
    private val ok = HttpStatusCode.OK
    private val created = HttpStatusCode.Created
    private val notFound = HttpStatusCode.NotFound


    private val server: NettyApplicationEngine = embeddedServer(Netty, port = 5555) {
        routing {
            get("/") {
                call.respondText("Default Endpoint")
            }

            //----------------------------------- User ------------------------------
            get("/users") {
                println("GET /user from ${call.request.origin.remoteHost}")
                val json = gson.toJson(UserService.getAllUsers())
                println("-- Send $json")
                call.respond(json)
            }

            get("/users/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                checkIdRange(id) {
                    call.respond(ok, gson.toJson(UserService.getUserByID(it)))
                }
            }

            put("/user/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val response = call.receive<String>()
                val responseUser = Gson().fromJson<User>(response, User::class.java)
                val moduleList = Gson().fromJson<ModuleList>(response, ModuleList::class.java)


                if (id != null && id in 1..100000) {
                    val json = gson.toJson(UserService.updateUser(responseUser, id))

                    val userToModulesList = mutableListOf<UserToModule>()
                    moduleList.modules.forEach { userToModulesList.add(UserToModule(id, it)) }

                    println("Modules: ${moduleList.modules} Response User ID: ${responseUser.id} ID: $id")

                    TempTableService.deleteModules(id)

                    userToModulesList.forEach { TempTableService.addModulesToUser(it) }

                    call.respond(ok, json)
                } else call.respond(notFound, "ID - Out of Range")
            }

            delete("/users/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()


                // Keine Benutzung von checkIDRange um shadowing zu vermeiden
                if (id != null && id in 1..100000) {
                    val courseList = CourseService.getAllCoursesFrom(id)

                    courseList.forEach { CourseService.deleteCourse(it.id) }

                    call.respond(ok, gson.toJson(UserService.deleteUser(id)))
                } else call.respond(notFound, "ID - Out of Range")

            }

            post("/user/register") {

                val response = call.receive<String>()
                println("/user/register - \n $response \n")
                val responseUser = Gson().fromJson<User>(response, User::class.java)
                val moduleList = Gson().fromJson<ModuleList>(response, ModuleList::class.java)
                val newUser = UserService.addUser(responseUser)


                val userToModulesList = mutableListOf<UserToModule>()
                moduleList.modules.forEach { userToModulesList.add(UserToModule(newUser.id, it)) }
                userToModulesList.forEach { TempTableService.addModulesToUser(it) }

                call.respond(created, Gson().toJson(newUser))
            }

            get("/users/{id}/courses") {
                val id = call.parameters["id"]?.toIntOrNull()

                checkIdRange(id) {
                    call.respond(ok, gson.toJson(CourseService.getAllCoursesFrom(it)))
                }
            }

            get("/users/{id}/modules") {
                val id = call.parameters["id"]?.toIntOrNull()
                println("/users/{id}/modules ID - $id")
                checkIdRange(id) {
                    call.respond(ok, gson.toJson(TempTableService.getModuleByUser(it)))
                }
            }


            //----------------------------------- Course ------------------------------
            get("/courses") {
                println("GET /courses from ${call.request.origin.remoteHost}")
                val json = gson.toJson(CourseService.getAllCourses())
                println("-- Send $json")
                call.respond(json)
            }

            get("/courses/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                checkIdRange(id) {
                    call.respond(ok, gson.toJson(CourseService.getCourseByID(it)))
                }
            }

            delete("/course/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                checkIdRange(id) {
                    call.respond(ok, gson.toJson(CourseService.deleteCourse(it)))
                }
            }

            put("/course/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val response = call.receive<String>()
                val responseCourse = Gson().fromJson<Course>(response, Course::class.java)
                val locationList = Gson().fromJson<LocationList>(response, LocationList::class.java)

                if (id != null && id in 1..100000) {
                    TempTableService.deleteLocations(id)

                    val json = gson.toJson(CourseService.updateCourse(responseCourse, id))

                    val locationToCourseList = mutableListOf<LocToCourse>()
                    locationList.locations.forEach { locationToCourseList.add(LocToCourse(id, it)) }

                    locationToCourseList.forEach { TempTableService.addLocationToCourse(it) }

                    call.respond(ok, json)
                } else call.respond(notFound, "ID - Out of Range")

            }

                post("/course/register") {
    
                    val response = call.receive<String>()
                    println("/course/register - \n $response \n")
                    val responseCourse = Gson().fromJson<Course>(response, Course::class.java)
                    val locationList = Gson().fromJson<LocationList>(response, LocationList::class.java)
                    val newCourse = CourseService.addCourse(responseCourse)
    
                    val locationToCourseList = mutableListOf<LocToCourse>()
                    locationList.locations.forEach { locationToCourseList.add(LocToCourse(newCourse.id, it)) }
                    locationToCourseList.forEach { TempTableService.addLocationToCourse(it) }
    
                    call.respond(created, Gson().toJson(newCourse))
                }


            get("/course/{id}/locations") {
                val id = call.parameters["id"]?.toIntOrNull()
                println("/course/{id}/locations ID - $id")
                checkIdRange(id) {
                    call.respond(ok, gson.toJson(TempTableService.getLocationByCourse(it)))
                }
            }

            get("/matching/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                checkIdRange(id) {
                    call.respond(ok, gson.toJson(MatchingService.findMatch(it)))
                }

            }

            get("/login/{email}"){
                val email = call.parameters["email"]
                if (email != null) call.respond(ok, gson.toJson(LoginService.getEmailData(email)))
            }
        }
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.checkIdRange(
        id: Int?, f: suspend (Int) -> Unit
    ) {

        if (id != null && id in 1..100000)
            f(id)
        else
            call.respond(notFound, "ID - Out of Range")
    }

    fun start() = server.start(false)
}
