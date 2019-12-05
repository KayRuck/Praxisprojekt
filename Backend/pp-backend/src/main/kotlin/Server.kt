import com.google.gson.Gson
import data.Course
import data.ModuleList
import data.User
import data.UserToModule
import database.CourseService
import database.DatabaseService
import database.TempTableService
import database.UserService
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


class Server(databaseService: DatabaseService) {

    private val gson = Gson()
    private val internalError = HttpStatusCode.InternalServerError
    private val ok = HttpStatusCode.OK
    private val created = HttpStatusCode.Created
    private val notFound = HttpStatusCode.NotFound


    private val server: NettyApplicationEngine = embeddedServer(Netty, port = 5555) {
        routing {
            get("/") {
                call.respondText("Hier soll was hin")
            }
            get("/users") {
                println("GET /user from ${call.request.origin.remoteHost}")
                val json = gson.toJson(UserService.getAllUsers())
                println("-- Send $json")
                call.respond(json)
            }

            get("/courses") {
                println("GET /courses from ${call.request.origin.remoteHost}")
                val json = gson.toJson(CourseService.getAllCourses())
                println("-- Send $json")
                call.respond(json)
            }

            /*
                        get("/users/{id}") {
                           val id = call.parameters["id"]?.toIntOrNull()

                           if (id != null && id in 1..100000) {
                               when (call.response.status()) {
                                   internalError -> call.respond(internalError, "Nutzer nicht vorhanden")
                                   oK -> call.respond( oK, gson.toJson(UserService.getUserByID(id)))
                               }

                           } else call.respond(notFount, "ID - Out of Range OR null")

                       }
           */

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

                checkIdRange(id) {
                    call.respond(ok, gson.toJson(UserService.updateUser(responseUser)))
                }


            }


/*
            get("/courses/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                checkIdRange(id) {
                    call.respond(ok, gson.toJson(CourseService.getCourseByID(it)))
                }
            }
*/
            post("/user/register") {

                val response = call.receive<String>()
                println("/user/register - \n $response \n")
                val responseUser = Gson().fromJson<User>(response, User::class.java)
                val moduleList = Gson().fromJson<ModuleList>(response, ModuleList::class.java)
                val newUser = UserService.addUser(responseUser)


                val userToModulesList = mutableListOf<UserToModule>()
                moduleList.modules.forEach { userToModulesList.add(UserToModule(newUser.id, it)) }
                userToModulesList.forEach { TempTableService.addModulesToUser(it) }



//                call.respond("Erhalten: $responseUser Nutzer: $newUser ID: ${newUser.id}")
                call.respond(Gson().toJson(newUser))
            }

            post("/course/register") {

                val response = call.receive<String>()
                println("/course/register - \n $response \n")
                val responseCourse = Gson().fromJson<Course>(response, Course::class.java)
                val newCourse = CourseService.addCourse(responseCourse)

//                call.respond("Erhalten: $responseUser Nutzer: $newUser ID: ${newUser.id}")
                call.respond(Gson().toJson(newCourse))
            }

            get("/users/{id}/courses") {
                val id = call.parameters["id"]?.toIntOrNull()

                checkIdRange(id) {
                    call.respond(ok, gson.toJson(CourseService.getCourseByID(it)))
                }

            }


            get("/users/{uid}/courses/{cid}") {
                val userID = call.parameters["uid"]?.toIntOrNull()
                val courseID = call.parameters["cid"]?.toIntOrNull()
                // TODO: Get a Courses form a User

            }

            get("/users/{id}/modules") {
                val id = call.parameters["id"]?.toIntOrNull()
                println("/users/{id}/modules ID - $id")
                checkIdRange(id) {
                    call.respond(ok, gson.toJson(TempTableService.getModuleByUser(it)))
                }
            }

            get("/matching/{id}&{module}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val module = call.parameters["module"]

                if (id != null && id in 1..100000) {
                    call.respond(HttpStatusCode.OK, gson.toJson(UserService.getUserByID(id)))


                } else call.respond(HttpStatusCode.NotFound, "ID - Out of Range")

            }

            /**
             * TODO: Save Contains Delete (For User --> all Courses)
             * TODO: Delete all Courses of the USer first and then the User
             *
             */

            delete("/users/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id != null && id in 1..100000)
                    call.respond(ok, gson.toJson(UserService.deleteUser(id)))
                else
                    call.respond(notFound, "ID - Out of Range")

            }

            delete("/course/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id != null && id in 1..100000)
                    call.respond(ok, gson.toJson(CourseService.deleteCourse(id)))
                else
                    call.respond(notFound, "ID - Out of Range")

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