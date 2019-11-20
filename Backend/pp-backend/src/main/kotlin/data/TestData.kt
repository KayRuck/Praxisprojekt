package data

import database.Courses
import database.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class TestData (){

    // TODO: Testdaten für alle Entitäten
    fun userCreation(){
        transaction {

            Users.insert {
                it[username] = "Paulchen"
                it[email] = "paulchen.panther@gmx.de"
                it[password] = " "
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }
/*          Funktioniert nicht

            Courses.insert{
                it[title] = "Mathe macht Spaß"
                it[description] = ""
                it[state] = true
                it[cLocLat] = 0.0
                it[cLocLang] = 0.0
                it[privateUsage] = true

                it[fk_creator] = 0
                it[fk_return] = 1
                it[fk_modules] = 0
            }
*/
            Users.insert {
                it[username] = "Obi-Wan"
                it[email] = "obi_wan.gibtsboni@gmx.de"
                it[password] = " "
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }

            Users.insert {
                it[username] = "GregGurke"
                it[email] = "Greg.dieGurke@gmx.de"
                it[password] = " "
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }


        }
    }

}