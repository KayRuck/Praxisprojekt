import database.DatabaseService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine


class Server (database: DatabaseService){

    // TODO: Bring den Server zum laufen, Netty Server mit Java 11 Unsafe bzw. die setAccessible(true)
    private val server : NettyApplicationEngine = embeddedServer(Netty, port = 5555) {
        routing {
            get("/users") {
                call.respond(database.getAllUsers())
                //call.respondText("Hier soll was hin", ContentType.Text.Plain)
            }
            post("/users") {
                // TODO: Mach das es Funktioniert
                // val user = call.receive<User>()
                call.respond(database.addUser(call.receive()))
            }
            get("/users/{id}") {
                val id = call.parameters["id"]?.toInt()!!

                if ( id > 0 || id < 100000 ){
                    call.respond(HttpStatusCode.OK)
                    call.respond(database.getUser(call.parameters["id"]?.toInt()!!)!!)
                }
                else call.respond(HttpStatusCode.NotFound)

            }
        }
    }

    fun start() = server.start(false)
}