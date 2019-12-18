package data

import database.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class TestData {

    fun userCreation() {
        transaction {

            Users.insert {
                it[username] = "Paulchen"
                it[email] = "paulchen.panther@gmx.de"
                it[description] = " "
                it[password] = " "
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }

            Users.insert {
                it[username] = "Obi-Wan"
                it[email] = "obi_wan.gibtsboni@gmx.de"
                it[description] = " "
                it[password] = " "
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }

            Users.insert {
                it[username] = "GregGurke"
                it[email] = "Greg.dieGurke@gmx.de"
                it[description] = " "
                it[password] = " "
                it[contact] = "0152 0000001"
                it[loc_lat] = 000.00
                it[loc_lang] = 000.00
            }

            Users.insert {
                it[username] = "Kay"
                it[email] = "Kayayay.kartoffel@gmx.de"
                it[description] = " "
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
                it[title] = "Kaffee"
                it[description] = "Ausgabe eines Kaffees (Zu Empfehlen bei kleineren Hilfen)"
            }

            TeachLocs.insert {
                it[title] = "Beim Tutors"
                it[description] = "Unterricht am Standort des Tutors"
            }

            TeachLocs.insert {
                it[title] = "Beim Schüler"
                it[description] = "Unterricht am Standort des Schülers"
            }

            TeachLocs.insert {
                it[title] = "An der TH"
                it[description] = "Unterricht am Standort des Lehrers"
            }

            TeachLocs.insert {
                it[title] = "Online Beratung"
                it[description] = "Unterricht über Skype, Whatsapp, etc."
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
                it[inReturnValue] = 10
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
                it[inReturnValue] = 10
                it[fk_creator] = 1
                it[fk_return] = 1
                it[fk_modules] = 1
            }
        }

        transaction {
            Courses.insert {
                it[title] = "Hier Gibt es BWL pur"
                it[description] = "BWL BWL BWL"
                it[state] = true
                it[cLocLat] = 0.0
                it[cLocLang] = 0.0
                it[privateUsage] = false
                it[inReturnValue] = 10
                it[fk_creator] = 4
                it[fk_return] = 1
                it[fk_modules] = 5
            }
        }

        transaction {
            Courses.insert {
                it[title] = "Mathe Kurs"
                it[description] = "Mathe Mathe Mathe"
                it[state] = true
                it[cLocLat] = 0.0
                it[cLocLang] = 0.0
                it[privateUsage] = true
                it[inReturnValue] = 10
                it[fk_creator] = 4
                it[fk_return] = 1
                it[fk_modules] = 2
            }
        }

        transaction {
            Courses.insert {
                it[title] = "Mathe für Wirtschaftsinformatik"
                it[description] = "Wirtschaftler"
                it[state] = true
                it[cLocLat] = 0.0
                it[cLocLang] = 0.0
                it[privateUsage] = true
                it[inReturnValue] = 10
                it[fk_creator] = 2
                it[fk_return] = 1
                it[fk_modules] = 4
            }
        }


        transaction {
            UserToModules.insert {
                it[fk_UserID] = 1
                it[fk_ModuleID] = 1
            }

            UserToModules.insert {
                it[fk_UserID] = 1
                it[fk_ModuleID] = 2
            }

            UserToModules.insert {
                it[fk_UserID] = 2
                it[fk_ModuleID] = 4
            }

            UserToModules.insert {
                it[fk_UserID] = 3
                it[fk_ModuleID] = 5
            }

            UserToModules.insert {
                it[fk_UserID] = 4
                it[fk_ModuleID] = 5
            }

            UserToModules.insert {
                it[fk_UserID] = 4
                it[fk_ModuleID] = 2
            }

            LocToCourses.insert {
                it[fk_TeachLocID] = 1
                it[fk_CourseID] = 1
            }

            LocToCourses.insert {
                it[fk_TeachLocID] = 1
                it[fk_CourseID] = 2
            }

            LocToCourses.insert {
                it[fk_TeachLocID] = 2
                it[fk_CourseID] = 3
            }

            LocToCourses.insert {
                it[fk_TeachLocID] = 3
                it[fk_CourseID] = 1
            }

            LocToCourses.insert {
                it[fk_TeachLocID] = 3
                it[fk_CourseID] = 2
            }

            LocToCourses.insert {
                it[fk_TeachLocID] = 3
                it[fk_CourseID] = 4
            }

            LocToCourses.insert {
                it[fk_TeachLocID] = 4
                it[fk_CourseID] = 1
            }
        }
    }

}