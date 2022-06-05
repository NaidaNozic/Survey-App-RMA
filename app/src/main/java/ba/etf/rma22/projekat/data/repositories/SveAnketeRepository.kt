package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Korisnik
import ba.etf.rma22.projekat.data.models.SveAnkete
import java.util.*

object SveAnketeRepository {

   /* fun postaviPitanjeIOdabranOdgovor(anketa:String,istrazivanje: String,tekst:String,odabranOdg:String){
        val anketa1= SveAnkete.ankete.find { a->a.naziv==anketa && a.nazivIstrazivanja==istrazivanje}?.pitanja
        val pitanje=anketa1?.get(tekst)
        if(pitanje!=null){
            pitanje.add(odabranOdg)
        }else{
            anketa1?.put(tekst, mutableListOf(odabranOdg))
        }
    }
    fun dajAnketu(anketa:String,istrazivanje:String,grupa:String): Anketa?{
        return SveAnkete.ankete.find { a-> a.nazivIstrazivanja==istrazivanje && a.nazivGrupe==grupa && a.naziv==anketa}
    }
    fun getOdgovore(anketa:String,istrazivanje:String,pitanje:String):MutableList<String>?{
        val odgovori= SveAnkete.ankete.find { a->a.naziv==anketa && a.nazivIstrazivanja==istrazivanje }?.pitanja?.get(pitanje)
        return odgovori
    }
    fun izmijeniDatumRada(anketa:String,istrazivanje:String){
        SveAnkete.ankete.find { a->a.naziv==anketa && a.nazivIstrazivanja==istrazivanje }?.datumRada= Date()
    }
    fun izmijeniProgres(anketa:String,istrazivanje:String,progres:Float){
        SveAnkete.ankete.find { a->a.naziv==anketa && a.nazivIstrazivanja==istrazivanje }?.progres=progres
    }
    fun dajSveAnkete():List<Anketa>{
        return SveAnkete.ankete
    }
  /*  fun dajMojeAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(i in Korisnik.upisanaIstrazivanja)
            for(g in Korisnik.upisaneGrupe)
                if(g.nazivIstrazivanja==i.naziv)
                    for(a in SveAnkete.ankete)
                        if(a.nazivIstrazivanja==i.naziv && a.nazivGrupe==g.naziv) rez.add(a)
        return rez
    }*/
    fun dajUradjeneAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(a in SveAnkete.ankete)
            if(a.progres==1F || a.datumRada != null)rez.add(a)
        return rez
    }
    fun dajBuduceAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(a in SveAnkete.ankete) {
            if (a.datumPocetak > Date()) rez.add(a)
            else if(a.datumKraj> Date() && a.datumPocetak< Date() && a.progres<1F && a.datumRada==null)rez.add(a)
        }
        return rez
    }
    fun dajProsleAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(a in SveAnkete.ankete)
            if (a.datumKraj< Date() && a.progres<1F)rez.add(a)
        return rez
    }*/
}