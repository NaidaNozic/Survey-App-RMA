package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.*
import java.util.*

class SveAnkete {
    companion object GlobalneAnkete{
        //ove ankete su globalne i mogu im atribute mijenjati
        var ankete= ankete().toMutableList()
    }
    fun postaviPitanjeIOdabranOdgovor(anketa:String,tekst:String,odabranOdg:String){
        val anketa1=ankete.find { a->a.naziv==anketa }?.pitanja
        val pitanje=anketa1?.get(tekst)
        if(pitanje!=null){
            pitanje.add(odabranOdg)
        }else{
            anketa1?.put(tekst, mutableListOf(odabranOdg))
        }
    }
    fun getOdgovore(anketa:String,pitanje:String):MutableList<String>?{
        val odgovori=ankete.find { a->a.naziv==anketa }?.pitanja?.get(pitanje)
        return odgovori
    }
    fun izmijeniDatumKraj(anketa:String){
        ankete.find { a->a.naziv==anketa }?.datumKraj= Date()
    }
    fun izmijeniProgres(anketa:String,istrazivanje:String,progres:Float){
        ankete.find { a->a.naziv==anketa && a.nazivIstrazivanja==istrazivanje }?.progres=progres
    }
    fun dajSveAnkete():List<Anketa>{
        return ankete
    }
    fun dajMojeAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(i in Korisnik.upisanaIstrazivanja)
            for(g in Korisnik.upisaneGrupe)
                if(g.nazivIstrazivanja==i.naziv)
                    for(a in ankete)
                        if(a.nazivIstrazivanja==i.naziv && a.nazivGrupe==g.naziv) rez.add(a)
        return rez
    }
    fun dajUradjeneAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(a in ankete)
            if(a.progres==1F && a.datumRada!=null)rez.add(a)
        return rez
    }
    fun dajBuduceAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(a in ankete) {
            if (a.datumPocetak > Date()) rez.add(a)
            else if(a.datumKraj> Date() && a.datumPocetak<Date() && a.progres<1F)rez.add(a)
        }
        return rez
    }
    fun dajProsleAnkete():MutableList<Anketa>{
        val rez= mutableListOf<Anketa>()
        for(a in ankete)
            if (a.datumKraj<Date() && a.progres<1F)rez.add(a)
        return rez
    }
}