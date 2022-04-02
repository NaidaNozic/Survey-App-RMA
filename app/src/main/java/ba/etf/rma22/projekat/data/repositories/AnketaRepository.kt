package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.ankete
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa
import java.util.*

object AnketaRepository {
    fun getAll() : List<Anketa> { //sve ankete
        val ankete = ankete().sortedBy { it.datumPocetak }
        return ankete;
    }
    fun getMyAnkete() : List<Anketa>{//sve moje ankete
        //ankete sa upisanim istrazivanjima i grupama
        var ankete=ankete().sortedBy{it.datumPocetak}.toMutableList()
        var rez= mutableListOf<Anketa>()

        for(i in IstrazivanjeRepository.getUpisani())
            for(g in GrupaRepository.getUpisani())
                for(a in ankete){
                    if(a.nazivIstrazivanja==i.naziv && a.nazivGrupe==g.naziv) rez.add(a)
                }
        return rez
    }
    fun getDone() : List<Anketa>{ //uradjene ankete
        //"ankete unutar korisnikovih grupa koje su urađene"
        //koliko sam ja shvatila to su sve ankete koje su urađene a pripadaju grupi u kojoj je korisnik upisan
        var ankete=ankete().sortedBy{it.datumPocetak}.toMutableList()
        var rez= mutableListOf<Anketa>()
        for(g in GrupaRepository.getUpisani())
            for(a in ankete)
                if(a.nazivGrupe==g.naziv && a.progres==1F && a.datumRada!=null
                    && a.datumKraj> Date() && a.datumPocetak< Date())rez.add(a)
        return rez
    }
    fun getFuture() : List<Anketa>{//buduce ankete
        //to su zelene i zute ankete sa grupama u kojima je korisnik upisan
        var ankete=ankete().sortedBy{it.datumPocetak}.toMutableList()
        var rez= mutableListOf<Anketa>()
        for(g in GrupaRepository.getUpisani())
            for(a in ankete)
                if(a.nazivGrupe==g.naziv){
                    if(a.datumKraj> Date() && a.datumPocetak<Date() && a.progres<1F && a.datumRada!=null)rez.add(a)
                    else if(a.datumPocetak>Date())rez.add(a)
                }
        return rez
    }
    fun getNotTaken() : List<Anketa>{//prosle(neuradjene) ankete
        //crvene ankete sa grupama u kojima je korisnik upisan
        var ankete=ankete().sortedBy{it.datumPocetak}.toMutableList()
        var rez= mutableListOf<Anketa>()
        for(g in GrupaRepository.getUpisani())
            for(a in ankete)
                if(a.nazivGrupe==g.naziv && a.datumKraj<Date() && a.progres<1F)rez.add(a)
        return rez
    }
}