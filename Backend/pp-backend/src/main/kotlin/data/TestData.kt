package data

import database.Courses
import database.InReturns
import database.Modules
import database.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TestData {

    // TODO: Testdaten für alle Entitäten
    fun userCreation() {
        transaction {

            Users.insert {
                it[username] = "Paulchen"
                it[email] = "paulchen.panther@gmx.de"
                it[password] = " "
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }

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

            Modules.insert {
                it[title] = "Algorithmen und Programmierung"
                it[description] = "Informatik"
            }

            Modules.insert {
                it[title] = "Mathematik 1"
                it[description] = "Informatik - TI, ITM, MI, AI"
            }

            Modules.insert {
                it[title] = "Mathematik 2"
                it[description] = "Informatik - TI, ITM, MI, AI"
            }

            Modules.insert {
                it[title] = "Mathematik"
                it[description] = "Informatik - WI"
            }

            Modules.insert {
                it[title] = "Betriebswirtschaftslehre 1"
                it[description] = "Informatik"
            }

            InReturns.insert {
                it[title] = "Geld"
                it[description] = "Ein festgelegter Stundenlohn"
            }

            InReturns.insert {
                it[title] = "Hilfe in anderen Fächern"
                it[description] = "Gegenseitige Hilfe in schwachen Fächern"
            }

            InReturns.insert {
                it[title] = "Ein Mensa essen"
                it[description] = "Ausgabe eines Mensaessens (Zu Empfehlen bei kleineren Hilfen)"
            }

            InReturns.insert {
                it[title] = "Geld"
                it[description] = "Ausgabe eines Kaffees (Zu Empfehlen bei kleineren Hilfen)"
            }


        }

        transaction {
            Courses.insert {
                it[title] = "Mathe macht Spaß"
                it[description] = ""
                it[state] = true
                it[cLocLat] = 0.0
                it[cLocLang] = 0.0
                it[privateUsage] = true
                it[fk_creator] = 1
                it[fk_return] = 1
                it[fk_modules] = 2
            }
        }
        transaction {
            Courses.insert {
                it[title] = "Hilfe in AP gewünscht"
                it[description] = "Ich brauch dringend hilfe in AP"
                it[state] = true
                it[cLocLat] = 0.0
                it[cLocLang] = 0.0
                it[privateUsage] = true
                it[fk_creator] = 1
                it[fk_return] = 1
                it[fk_modules] = 1
            }
        }
    }

}