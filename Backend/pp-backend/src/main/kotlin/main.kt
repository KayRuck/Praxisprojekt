import data.TestData
import database.DatabaseService

fun main() {

    val db = DatabaseService()

   //Testing
    val test = TestData()
    test.userCreation()

    val server = Server(db)
    server.start()
}