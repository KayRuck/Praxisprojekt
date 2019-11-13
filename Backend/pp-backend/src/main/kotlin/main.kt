import database.DatabaseService

fun main() {

    val db = DatabaseService()
    val test = database.TestData()
    test.creation()
    val server = Server(db)
    server.start()
}