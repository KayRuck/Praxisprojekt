import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

class Server {

    val database = Database.Companion.connect("jdbc:mysql://localhost:3306/praxisprojekt",
            driver = "com.mysql.jdbc.Driver", user = "root", password = "")



    private val server : NettyApplicationEngine = embeddedServer(Netty, port = 5555) {
        routing {
            get("/") {
                call.respondText("Hallo Welt", ContentType.Text.Plain)

            }
        }
    }

    fun start() = server.start(false)
}