package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.*
import java.util.*

class SveAnkete {
    companion object GlobalneAnkete{
        //ove ankete su globalne i mogu im atribute mijenjati
        var ankete= ankete()
        var mojeAnkete= myAnkete()
        var uradjene= doneAnkete()
        var buduce= futureAnkete()
        var prosle= notTakenAnkete()
    }
    fun postaviPitanjeIOdabranOdgovor(anketa:String,tekst:String,odabranOdg:String){
        var anketa1=ankete.find { a->a.naziv==anketa }?.pitanja
        var pitanje=anketa1?.get(tekst)
        if(pitanje!=null){
            pitanje.add(odabranOdg)
        }else{
            anketa1?.put(tekst, mutableListOf(odabranOdg))
        }
    }
    fun getOdgovore(anketa:String,pitanje:String):MutableList<String>?{
        var odgovori=ankete.find { a->a.naziv==anketa }?.pitanja?.get(pitanje)
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
    fun dajMojeAnkete():List<Anketa>{
        return mojeAnkete
    }
    fun dajUradjeneAnkete():List<Anketa>{
        return uradjene
    }
    fun dajBuduceAnkete():List<Anketa>{
        return buduce
    }
    fun dajProsleAnkete():List<Anketa>{
        return prosle
    }
}