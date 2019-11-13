package database

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class TestData (){

    // TODO: Testdaten für alle Entitäten
    fun creation(){
        transaction {

            Users.insert {
                it[username] = "Paulchen"
                it[email] = "paulchen.panther@gmx.de"
                it[password] = "sUp3RP4Ul!"
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }
        }
    }



}