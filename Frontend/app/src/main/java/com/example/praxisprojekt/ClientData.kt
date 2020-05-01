package com.example.praxisprojekt

enum class Mods (val id: Int, var title : String, var desc : String?) {

    APMOD(1, "Algorithmen und Programmierung", "Informatik"),
    MATH1INFMOD (2, "Mathematik 1", "Informatik - TI, ITM, MI, AI"),
    MATH2INFMOD (3, "Mathematik 2", "Informatik - TI, ITM, MI, AI"),
    MATHINFMOD (4, "Mathematik", "Informatik - WI"),
    BWL1INFMOD (5, "Betriebswirtschaftslehre 1", "Informatik")
}

enum class TeachLocations (val id: Int, var title : String, var desc : String?){

    TEACH(1, "Tutor", "Unterricht am Standort des Tutors"),
    STUD(2, "Studenten", "Unterricht am Standort des Lehrers"),
    TH(3, "Technische Hochschule", "Unterricht am Standort des Lehrers"),
    ONLINE(4, "Online", "Unterricht über Skype, Whatsapp, etc.")
}

enum class InReturns (val id: Int, var title : String, var desc : String?){
    MONEY(1, "Bezahlung", ""),
    HELP(2, "Hilfe in anderen Fächern", ""),
    MENSA(3, "Ein Essen in der Mensa", ""),
    COFFEE(4, "Kaffee", "")
}

enum class Constants(val string: String){
    API_BASE_URL("http://192.168.0.185:5555"),
    API_BASE_URL_LAPTOP("http://192.168.0.45:5555"),
    API_BASE_URL_P("http://192.168.0.18:5555"),
    API_BASE_URL_W("http://192.168.137.1:5555"),
    PATTERN("(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,})"),
}