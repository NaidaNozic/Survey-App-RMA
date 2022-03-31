package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Anketa
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

fun ankete(): List<Anketa> {
    var cal: Calendar = Calendar.getInstance()
    cal.set(2021,3,10)
    var dat1: Date = cal.time;

    cal.set(2021,4,10)
    var dat2: Date = cal.time;

    cal.set(2021,3,12)
    var dat3: Date = cal.time;
    //
    cal.set(2018,3,10)
    var dat4: Date = cal.time;

    cal.set(2018,4,10)
    var dat5: Date = cal.time;

    cal.set(2018,3,13)
    var dat6: Date = cal.time;
    //
    cal.set(2023,2,10)
    var dat7: Date = cal.time;

    cal.set(2023,4,10)
    var dat8: Date = cal.time;

    cal.set(2023,3,13)
    var dat9: Date = cal.time;
    return listOf(
        Anketa("Anketa 1","Istraživanje broj 1",
            dat1,dat2,dat3,30,"Grupa 1",0.6F),
        Anketa("Anketa 2","Istraživanje broj 1",
            dat1,dat2,dat3, 56,"Grupa 2",0.4F),
        Anketa("Anketa 2","Istraživanje broj 2",
            dat4,dat5,dat6, 34,"Grupa 3",0.9F),
        Anketa("Anketa 3","Istraživanje broj 2",
            dat4,dat5,dat6,4,"Grupa 4",0.8F),
        Anketa("Anketa 3","Istraživanje broj 7",
            dat4,dat5,dat6, 7,"Grupa 6",1F),
        Anketa("Anketa 1","Istraživanje broj 5",
            dat7,dat8,dat9, 7,"Grupa 7",0.2F),
        Anketa("Anketa 1","Istraživanje broj 1",
            dat7,dat8,dat9, 30,"Grupa 1",0.6F),
        Anketa("Anketa 2","Istraživanje broj 1",
            dat7,dat8,dat8, 56,"Grupa 2",0.4F),
        Anketa("Anketa 2","Istraživanje broj 2",
            dat1,dat2,dat3, 34,"Grupa 3",0.9F),
        Anketa("Anketa 3","Istraživanje broj 2",
            dat4,dat5,dat6, 4,"Grupa 4",0.8F),
        Anketa("Anketa 3","Istraživanje broj 7",
            dat4,dat5,dat6, 7,"Grupa 6",1F)
    )
}