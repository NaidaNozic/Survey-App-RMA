package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.PitanjeAnketa

fun pitanja():List<Pitanje>{
    return listOf(Pitanje("Pitanje 1","Kako se ja zovem?", listOf("Amela","Naida","Selma")),
    Pitanje("Pitanje 2","Kako se ja prezivam?", listOf("Mehić","Pita","Nožić")),
    Pitanje("Pitanje 3","Koji jezik je preporučen za razvoj Android aplikacija?", listOf("Java","Kotlin","C#")),
    Pitanje("Pitanje 4","Koja je najbliža planeta Suncu?",listOf("Zemlja","Jupiter","Merkur")),
    Pitanje("Pitanje 5","Koji tip podatka čuva cijele brojeve?",listOf("Int","String","Bool")),
    Pitanje("Pitanje 6","Najduža rijeka na Zemlji je:",listOf("Nil","Amazon","Miljacka")),
    Pitanje("Pitanje 7","Glavni grad BiH je:", listOf("Mostar","Sarajevo","Banja Luka")),
    Pitanje("Pitanje 8","Najbrža životinja na svijetu je:",listOf("Slon","Lav","Gepard")),
    Pitanje("Pitanje 9","Koliko ima kontinenata?", listOf("10","8","7")),
    Pitanje("Pitanje 10","Koliko dana ima u godini?", listOf("366","365","367")))
}
fun pitanjaPoAnketi():List<PitanjeAnketa>{
    return listOf(PitanjeAnketa("Pitanje 1","Anketa 1"),PitanjeAnketa("Pitanje 2","Anketa 1"),
        PitanjeAnketa("Pitanje 2","Anketa 2"),PitanjeAnketa("Pitanje 3","Anketa 2"),
        PitanjeAnketa("Pitanje 4","Anketa 2"),PitanjeAnketa("Pitanje 10","Anketa 2"),
        PitanjeAnketa("Pitanje 3","Anketa 3"),PitanjeAnketa("Pitanje 1","Anketa 3"),
        PitanjeAnketa("Pitanje 5","Anketa 3"),PitanjeAnketa("Pitanje 9","Anketa 3"),
        PitanjeAnketa("Pitanje 4","Anketa 4"),
        PitanjeAnketa("Pitanje 5","Anketa 5"),PitanjeAnketa("Pitanje 1","Anketa 5"),
        PitanjeAnketa("Pitanje 8","Anketa 5"),PitanjeAnketa("Pitanje 2","Anketa 6"),
        PitanjeAnketa("Pitanje 6","Anketa 6"),PitanjeAnketa("Pitanje 7","Anketa 6"),
        PitanjeAnketa("Pitanje 5","Anketa 6"),
        PitanjeAnketa("Pitanje 7","Anketa 7"),PitanjeAnketa("Pitanje 1","Anketa 7"),
        PitanjeAnketa("Pitanje 8","Anketa 8"),
        PitanjeAnketa("Pitanje 9","Anketa 9"),PitanjeAnketa("Pitanje 2","Anketa 9"),
        PitanjeAnketa("Pitanje 8","Anketa 9"),PitanjeAnketa("Pitanje 7","Anketa 9"),
        PitanjeAnketa("Pitanje 6","Anketa 9"),
        PitanjeAnketa("Pitanje 10","Anketa 10"), PitanjeAnketa("Pitanje 6","Anketa 14"),
        PitanjeAnketa("Pitanje 10","Anketa 14"), PitanjeAnketa("Pitanje 6","Anketa 15"),
        PitanjeAnketa("Pitanje 10","Anketa 15"), PitanjeAnketa("Pitanje 6","Anketa 16"),
        PitanjeAnketa("Pitanje 10","Anketa 16"))
}