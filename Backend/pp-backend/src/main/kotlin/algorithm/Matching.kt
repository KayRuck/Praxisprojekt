package algorithm

import data.Mod
import data.User
import database.Users

class Matching {

    /**
     * Zwei Listen an.
     *
     * Eine enthält die Daten (ID und Fächer) des Clients
     * Mit der anderen wird durch alle im Server befindlichen Entitäten durchlaufen und die akutellen Daten gespeichter
     * über die ID werden die eigenen Anzeigen des Clients raus gefiltert
     *
     *
     *
     */


    fun findMatch(objectiveUser : User, currentModule : Mod){
        val userList = listOf<User>()

//        userList.forEach{ it.moduleList.foreach{ it == currentModule then giveback User.id} }
        // ID werden in einer zweiten Liste gespeichert und an den Server zurück gegeben

    }

}