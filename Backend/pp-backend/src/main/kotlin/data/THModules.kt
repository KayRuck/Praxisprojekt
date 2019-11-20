package data

enum class Mods (val id: Int, var title : String, var desc : String?) {

    APMOD(0, "Algorithmen und Programmierung", "Informatik"),
    MATH1INFMOD (1, "Mathematik 1", "Informatik - TI, ITM, MI, AI"),
    MATH2INFMOD (2, "Mathematik 2", "Informatik - TI, ITM, MI, AI"),
    MATHINFMOD (3, "Mathematik", "Informatik - WI"),
    BWL1INFMOD (4, "Betriebswirtschaftslehre 1", "Informatik")
}

enum class TeachLocs (val id: Int, var title : String, var desc : String?){

    TEACH(0, "Beim Lehrer", ""),
    STUD(1, "Beim Studenten", "Informatik - TI, ITM, MI, AI"),
    TH(2, "In der TH", "Informatik - TI, ITM, MI, AI"),
    ONLINE(3, "Online Beratung", "Informatik - WI")
}

enum class InReturns (val id: Int, var title : String, var desc : String?){
    MONEY(0, "Geld", ""),
    HELP(1, "Hilfe in anderen Fächern", ""),
    MENSA(2, "Ein Mensa essen", "")
}