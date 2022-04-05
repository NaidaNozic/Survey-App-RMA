package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.util.*

class AnketaRepositoryTest {

    @Test
    fun getAll() {
        var lista=AnketaRepository.getAll()
       val calendar= Calendar.getInstance()
        calendar.time=lista.get(0).datumPocetak
        assertTrue(calendar.get(Calendar.YEAR)==2018 && calendar.get(Calendar.MONTH)==3 &&
                   calendar.get(Calendar.DAY_OF_MONTH)==10)

        calendar.time=lista.get(lista.size-1).datumPocetak
        assertTrue(calendar.get(Calendar.YEAR)==2023 && calendar.get(Calendar.MONTH)==2 &&
                   calendar.get(Calendar.DAY_OF_MONTH)==10)
    }

    @Test
    fun getMyAnkete() {
        var cal: Calendar = Calendar.getInstance()

        cal.set(2018,3,10)
        var dat4: Date = cal.time;

        cal.set(2018,4,10)
        var dat5: Date = cal.time;

        cal.set(2018,5,13)
        var dat6: Date = cal.time;
        //
        cal.set(2023,2,10)
        var dat7: Date = cal.time;

        cal.set(2023,4,10)
        var dat8: Date = cal.time;

        cal.set(2022,7,13)
        var dat10: Date = cal.time;
        var ocekivano=listOf(
            Anketa("Anketa 9","Istraživanje broj 3",
            dat4,dat10,dat6, 34,"Grupa 9",1F),
            Anketa("Anketa 3","Istraživanje broj 2",
            dat4,dat5,dat6, 34,"Grupa 3",0.9F),
            Anketa("Anketa 4","Istraživanje broj 2",
            dat4,dat5,dat6,4,"Grupa 4",0.8F),
            Anketa("Anketa 8","Istraživanje broj 4",
            dat7,dat8,dat8, 56,"Grupa 8",0.4F),
            Anketa("Anketa 14","Istraživanje broj 3", //zuta
                dat4,dat10,null, 34,"Grupa 9",0.3F)).toMutableList().
        map { o->o.nazivIstrazivanja+" "+o.nazivGrupe }

        var dobijene=AnketaRepository.getMyAnkete().toMutableList().map { d->d.nazivIstrazivanja+" "+d.nazivGrupe }
        for(d in dobijene)println(d)
        assertTrue(ocekivano.size==dobijene.size)
        assertTrue(ocekivano.containsAll(dobijene))
    }

    @Test
    fun testGetMyAnkete() {

        var o1=listOf("Istraživanje broj 5 Grupa 5",
            "Istraživanje broj 1 Grupa 1",
            "Istraživanje broj 5 Grupa 6")

        var dobijene=AnketaRepository.getMyAnkete(listOf(Istrazivanje("Istraživanje broj 1",1),
            Istrazivanje("Istraživanje broj 5",5)),
            listOf(Grupa("Grupa 5","Istraživanje broj 5"),
                Grupa("Grupa 6","Istraživanje broj 5"),
                Grupa("Grupa 1","Istraživanje broj 1"))).
        toMutableList().map { d->d.nazivIstrazivanja+" "+d.nazivGrupe }

        assertTrue(o1.size==dobijene.size)
        assertTrue(dobijene.containsAll(o1))
    }

    @Test
    fun getDone() {
        var cal: Calendar = Calendar.getInstance()
        cal.set(2018,3,10)
        var dat4: Date = cal.time;
        cal.set(2022,7,13)
        var dat10: Date = cal.time;
        cal.set(2018,5,13)
        var dat6: Date = cal.time;
        var ocekivano= listOf(Anketa("Anketa 9","Istraživanje broj 3",
            dat4,dat10,dat6, 34,"Grupa 9",1F))

        var dobijeno=AnketaRepository.getDone()

        assertTrue(ocekivano.size==dobijeno.size)
        assertTrue(ocekivano.get(0).nazivIstrazivanja==dobijeno.get(0).nazivIstrazivanja &&
                   ocekivano.get(0).nazivGrupe==dobijeno.get(0).nazivGrupe)
    }

    @Test
    fun testGetDone() {
        var dobijene=AnketaRepository.getDone(
            listOf(Grupa("Grupa 7","Istraživanje broj 4"),
                Grupa("Grupa 9","Istraživanje broj 3"),
                Grupa("Grupa 10","Istraživanje broj 3"))).toMutableList().
        map { d->d.nazivIstrazivanja+" "+d.nazivGrupe }

        var cal: Calendar = Calendar.getInstance()
        cal.set(2018,3,10)
        var dat4: Date = cal.time;
        cal.set(2022,7,13)
        var dat10: Date = cal.time;
        cal.set(2018,5,13)
        var dat6: Date = cal.time;

        var ocekivane= listOf( Anketa("Anketa 7","Istraživanje broj 4", //plava
                dat4,dat10,dat6, 30,"Grupa 7",1F),
            Anketa("Anketa 9","Istraživanje broj 3", //plava
                dat4,dat10,dat6, 34,"Grupa 9",1F),
            Anketa("Anketa 10","Istraživanje broj 3",
                dat4,dat10,dat6, 4,"Grupa 10",1F)).toMutableList().
        map { o->o.nazivIstrazivanja+" "+o.nazivGrupe }

        assertTrue(dobijene.size==ocekivane.size)
        assertTrue(ocekivane.containsAll(dobijene))
    }

    @Test
    fun getFuture() {
        var cal: Calendar = Calendar.getInstance()

        cal.set(2018,3,10)
        var dat4: Date = cal.time;

        cal.set(2023,2,10)
        var dat7: Date = cal.time;

        cal.set(2023,4,10)
        var dat8: Date = cal.time;

        cal.set(2022,7,13)
        var dat10: Date = cal.time;

        var ocekivane=listOf(
            Anketa("Anketa 8","Istraživanje broj 4", //zuta
                dat7,dat8,dat8, 56,"Grupa 8",0.4F),
            Anketa("Anketa 14","Istraživanje broj 3", //zuta
                dat4,dat10,null, 34,"Grupa 9",0.3F)
        ).toMutableList().map { o->o.nazivIstrazivanja+" "+o.nazivGrupe }

        var dobijene=AnketaRepository.getFuture().toMutableList().map { d->d.nazivIstrazivanja+" "+d.nazivGrupe }

        assertTrue(ocekivane.size==dobijene.size)
        assertTrue(ocekivane.containsAll(dobijene))
    }

    @Test
    fun testGetFuture() {
        var cal: Calendar = Calendar.getInstance()

        cal.set(2023,2,10)
        var dat7: Date = cal.time;

        cal.set(2023,4,10)
        var dat8: Date = cal.time;

        cal.set(2023,3,13)
        var dat9: Date = cal.time;

        cal.set(2018,3,10)
        var dat4: Date = cal.time;

        cal.set(2022,7,13)
        var dat10: Date = cal.time;

        var grupe=listOf(Grupa("Grupa 9","Istraživanje broj 3"),
            Grupa("Grupa 3","Istraživanje broj 2"),
            Grupa("Grupa 4","Istraživanje broj 2"),
            Grupa("Grupa 6","Istraživanje broj 5"),
            Grupa("Grupa 5","Istraživanje broj 5"))

        var dobijene=AnketaRepository.getFuture(grupe).toMutableList().map { d->d.nazivIstrazivanja+" "+d.nazivGrupe }

        var ocekivane= listOf(Anketa("Anketa 6","Istraživanje broj 5",
            dat7,dat8,dat9, 7,"Grupa 6",0.2F),
            Anketa("Anketa 14","Istraživanje broj 3", //zuta
                dat4,dat10,null, 34,"Grupa 9",0.0F)).toMutableList().
        map { o->o.nazivIstrazivanja+" "+o.nazivGrupe }

        assertTrue(ocekivane.size==dobijene.size)
        assertTrue(ocekivane.containsAll(dobijene))
    }

    @Test
    fun getNotTaken() {
        var cal: Calendar = Calendar.getInstance()

        cal.set(2018,3,10)
        var dat4: Date = cal.time;

        cal.set(2018,4,10)
        var dat5: Date = cal.time;

        cal.set(2018,3,13)
        var dat6: Date = cal.time;

        var dobijene=AnketaRepository.getNotTaken().toMutableList().map { d->d.nazivIstrazivanja+" "+d.nazivGrupe }

        var ocekivane=listOf(Anketa("Anketa 3","Istraživanje broj 2",
            dat4,dat5,dat6, 34,"Grupa 3",0.9F),
            Anketa("Anketa 4","Istraživanje broj 2",
            dat4,dat5,dat6,4,"Grupa 4",0.8F)
        ).toMutableList().map { o->o.nazivIstrazivanja+" "+o.nazivGrupe }

        assertTrue(ocekivane.size==dobijene.size)
        assertTrue(ocekivane.containsAll(dobijene))
    }

    @Test
    fun testGetNotTaken() {
        var cal: Calendar = Calendar.getInstance()

        cal.set(2018,3,10)
        var dat4: Date = cal.time;

        cal.set(2018,4,10)
        var dat5: Date = cal.time;

        cal.set(2018,3,13)
        var dat6: Date = cal.time;
        var grupe = listOf(
            Grupa("Grupa 9", "Istraživanje broj 3"),
            Grupa("Grupa 3", "Istraživanje broj 2"),
            Grupa("Grupa 4", "Istraživanje broj 2"),
            Grupa("Grupa 8", "Istraživanje broj 4"),
            Grupa("Grupa 5","Istraživanje broj 5")
        )
        var dobijene = AnketaRepository.getNotTaken(grupe).toMutableList().map {d->d.nazivIstrazivanja+" "+d.nazivGrupe }

        var ocekivane= listOf(Anketa("Anketa 4","Istraživanje broj 2",
            dat4,dat5,dat6,4,"Grupa 4",0.8F),
            Anketa("Anketa 5","Istraživanje broj 5",
                dat4,dat5,dat6, 7,"Grupa 5",0.3F),
            Anketa("Anketa 3","Istraživanje broj 2", //crvena
                dat4,dat5,dat6, 34,"Grupa 3",0.9F)).toMutableList().
        map { o->o.nazivIstrazivanja+" "+o.nazivGrupe }

        assertTrue(ocekivane.size==dobijene.size)
        assertTrue(ocekivane.containsAll(dobijene))
    }
}