import com.google.gson.Gson
import data.User
import database.CourseService
import database.DatabaseService
import database.UserService
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine


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

                if (id != null && id in 1..100000)
                    call.respond(ok, gson.toJson(UserService.getUserByID(id)))
                else
                    call.respond(notFound, "ID - Out of Range")

            }

            get("/courses/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id != null && id in 1..100000)
                    call.respond(ok, gson.toJson(CourseService.getCourseByID(id)))
                else
                    call.respond(notFound, "ID - Out of Range")

            }

            post("/user/register") {

                val responseUser = call.receive<User>()
                val newUser = UserService.addUser(responseUser)

                call.respond("Erhalten: $responseUser Nuutzer: $newUser ID: ${newUser.id}")
            }

            // TODO: Get all Courses form a User
            get("/users/{id}/courses") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id != null && id in 1..100000)
                    call.respond(ok, gson.toJson(CourseService.getCourseByID(id)))
                else
                    call.respond(notFound, "ID - Out of Range")

            }


            get("/users/{uid}/courses/{cid}") {
                val userID = call.parameters["uid"]?.toIntOrNull()
                val courseID = call.parameters["cid"]?.toIntOrNull()
                // TODO: Get a Courses form a User

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

    fun start() = server.start(false)
}