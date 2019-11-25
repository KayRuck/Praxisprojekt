import com.google.gson.Gson
import data.User
import database.CourseService
import database.DatabaseService
import database.UserService
import database.Users
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine


class Server (databaseService: DatabaseService){

    private val gson = Gson()

    private val server : NettyApplicationEngine = embeddedServer(Netty, port = 5555) {
        routing {
            get("/") {
                call.respondText("Hier soll was hin")
            }
            get("/users") {

                call.respond(gson.toJson(UserService.getAllUsers()))
            }
            get("/courses") {
                println("GET /courses from ${call.request.origin.remoteHost}")
                val json = gson.toJson(CourseService.getAllCourses())
                println("-- Send $json")
                call.respond(json)
            }
            get("/course") {
                val id = call.parameters["id"]?.toIntOrNull()

                if ( id != null && id in 1..100000)
                    call.respond(HttpStatusCode.OK, gson.toJson(CourseService.getCourseByID(id)!!))
                else
                    call.respond(HttpStatusCode.NotFound, "ID - Out of Range")

            }
            get("/users") {
                val id = call.parameters["id"]?.toIntOrNull()

                if ( id != null && id in 1..100000)
                    call.respond(HttpStatusCode.OK, gson.toJson(UserService.getUserByID(id)!!))
                else
                    call.respond(HttpStatusCode.NotFound, "ID - Out of Range")

            }

            post("/user/register") {

                //val json = gson.fromJson<User>(call.receive<User>()., User.class)
                //UserService.addUser(call.receive<User>())
                //call.respond(HttpStatusCode.Created)
                call.respond("Erhalten: " + call.receive())
            }


            get("/matching/{id}&{Module}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if ( id != null && id in 1..100000)
                    call.respond(HttpStatusCode.OK, gson.toJson(UserService.getUserByID(id)!!))
                else
                    call.respond(HttpStatusCode.NotFound, "ID - Out of Range")

            }


        }
    }

    fun start() = server.start(false)
}