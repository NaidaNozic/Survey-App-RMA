package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Anketa
import java.util.*

fun ankete(): List<Anketa> {
    var cal: Calendar = Calendar.getInstance()
    cal.set(2021,3,10)
    val dat1: Date = cal.time

    cal.set(2021,4,10)
    val dat2: Date = cal.time

    cal.set(2018,3,10)
    val dat4: Date = cal.time

    cal.set(2018,4,10)
    val dat5: Date = cal.time

    cal.set(2018,3,13)
    val dat6: Date = cal.time
    //
    cal.set(2023,2,10)
    val dat7: Date = cal.time

    cal.set(2023,4,10)
    val dat8: Date = cal.time

    cal.set(2022,7,13)
    val dat10: Date = cal.time
    //jedna anketa po grupi
    return listOf(
        Anketa("Anketa 1","Istraživanje broj 1", //crvena
            dat1,dat2,null,30,"Grupa 1",0.6F, mutableMapOf("Kako se ja zovem?" to mutableListOf("Amela"))),
        Anketa("Anketa 1","Istraživanje broj 2", //crvena
            dat1,dat2,null,30,"Grupa 3",0.6F, mutableMapOf("Kako se ja zovem?" to mutableListOf("Amela"))),
        Anketa("Anketa 2","Istraživanje broj 1", //zelena
            dat4,dat10,null, 56,"Grupa 2",0.4F),
        Anketa("Anketa 2","Istraživanje broj 3", //zelena
            dat4,dat10,null, 56,"Grupa 10",0.4F),
        Anketa("Anketa 3","Istraživanje broj 2", //crvena
            dat4,dat5,null, 34,"Grupa 3",0.9F, mutableMapOf("Kako se ja prezivam?" to mutableListOf("Pita"))),
        Anketa("Anketa 4","Istraživanje broj 2", //crvena
            dat4,dat5,null,4,"Grupa 4",0.8F, mutableMapOf("Kako se ja prezivam?" to mutableListOf("Nožić"))),
        Anketa("Anketa 5","Istraživanje broj 5", //zuta
            dat4,dat5,null, 7,"Grupa 5",0.3F, mutableMapOf("Kako se ja prezivam?" to mutableListOf("Mehić"))),
        Anketa("Anketa 6","Istraživanje broj 5", //zuta
            dat7,dat8,null, 7,"Grupa 6",0.2F),
        Anketa("Anketa 7","Istraživanje broj 4", //plava
            dat4,dat10,dat6, 30,"Grupa 7",1F,mutableMapOf("Kako se ja zovem?" to mutableListOf("Naida"))),
        Anketa("Anketa 8","Istraživanje broj 4", //zuta
            dat7,dat8,null, 56,"Grupa 8",0.4F),
        Anketa("Anketa 14","Istraživanje broj 3", //zuta
            dat4,dat10,null, 34,"Grupa 9",0.0F),
        Anketa("Anketa 9","Istraživanje broj 3", //plava
            dat4,dat10,dat6, 34,"Grupa 9",1F,mutableMapOf("Koliko ima kontinenata?" to mutableListOf("8"))),
        Anketa("Anketa 10","Istraživanje broj 3", //plava
            dat4,dat10,dat6, 4,"Grupa 10",1F,mutableMapOf("Koliko dana ima u godini?" to mutableListOf("365"))),
        Anketa("Anketa 15","Istraživanje broj 3", //zuta
            dat7,dat10,null, 34,"Grupa 10",1F),
        Anketa("Anketa 16","Istraživanje broj 1", //zelena
            dat4,dat10,null, 56,"Grupa 2",0.4F)
    )
}