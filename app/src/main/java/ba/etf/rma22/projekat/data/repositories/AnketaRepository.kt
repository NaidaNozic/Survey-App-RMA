package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.SveAnkete

object AnketaRepository {

    fun getAll() : List<Anketa> { //sve ankete
        return SveAnketeRepository.dajSveAnkete().sortedBy{it.datumPocetak};
    }

    fun getMyAnkete() : List<Anketa>{//sve moje ankete
        //ankete sa upisanim istrazivanjima i grupama
        return SveAnketeRepository.dajMojeAnkete().toMutableList().sortedBy{it.datumPocetak}
    }
    fun getMyAnkete(dodatnaIstr: List<Istrazivanje>,dodatneGrupe:List<Grupa>) : List<Anketa>{//sve moje ankete
        val ankete=SveAnketeRepository.dajSveAnkete()
        var rez= mutableListOf<Anketa>()

        for(i in dodatnaIstr)
            for(g in dodatneGrupe)
                if(i.naziv==g.nazivIstrazivanja)
                for(a in ankete){
                    if(a.nazivIstrazivanja==i.naziv && a.nazivGrupe==g.naziv && !rez.contains(a)) rez.add(a)
                }
        rez=rez.sortedBy{it.datumPocetak}.toMutableList()
        return rez
    }
    fun getDone() : List<Anketa>{ //uradjene ankete
        //"ankete unutar korisnikovih grupa koje su urađene"
        //koliko sam ja shvatila to su sve ankete koje su urađene a pripadaju grupi u kojoj je korisnik upisan
        val ankete= SveAnketeRepository.dajUradjeneAnkete()
        var rez= mutableListOf<Anketa>()
        for(g in GrupaRepository.getUpisani())
            for(a in ankete)
                if(a.nazivGrupe==g.naziv && a.datumRada != null && !rez.contains(a))rez.add(a)
        rez=rez.sortedBy{it.datumPocetak}.toMutableList()
        return rez
    }
    fun getDone(dodatne:List<Grupa>) : List<Anketa>{
        val ankete= SveAnketeRepository.dajUradjeneAnkete()
        var rez= mutableListOf<Anketa>()
        for(g in dodatne)
            for(a in ankete)
                if(a.nazivGrupe==g.naziv &&  a.datumRada != null && !rez.contains(a))rez.add(a)
        rez=rez.sortedBy{it.datumPocetak}.toMutableList()
        return rez
    }
    fun getFuture() : List<Anketa>{//buduce ankete
        //to su zelene i zute ankete sa grupama u kojima je korisnik upisan
        val ankete= SveAnketeRepository.dajBuduceAnkete()
        var rez= mutableListOf<Anketa>()
        for(g in GrupaRepository.getUpisani())
            for(a in ankete)
                if(a.nazivGrupe==g.naziv && !rez.contains(a))
                    rez.add(a)
        rez=rez.sortedBy{it.datumPocetak}.toMutableList()
        return rez
    }
    fun getFuture(dodatneGrupe: List<Grupa>) : List<Anketa>{
        val ankete= SveAnketeRepository.dajBuduceAnkete()
        var rez= mutableListOf<Anketa>()
        for(g in dodatneGrupe)
            for(a in ankete)
                if(a.nazivGrupe==g.naziv && !rez.contains(a))rez.add(a)
        rez=rez.sortedBy{it.datumPocetak}.toMutableList()
        return rez
    }
    fun getNotTaken() : List<Anketa>{//prosle(neuradjene) ankete
        //crvene ankete sa grupama u kojima je korisnik upisan
        val ankete= SveAnketeRepository.dajProsleAnkete()
        var rez= mutableListOf<Anketa>()
        for(g in GrupaRepository.getUpisani())
            for(a in ankete)
                if(a.nazivGrupe==g.naziv && !rez.contains(a))rez.add(a)
        rez=rez.sortedBy{it.datumPocetak}.toMutableList()
        return rez
    }
    fun getNotTaken(dodatneGrupe: List<Grupa>) : List<Anketa>{
        val ankete= SveAnketeRepository.dajProsleAnkete()
        var rez= mutableListOf<Anketa>()
        for(g in dodatneGrupe)
            for(a in ankete)
                if(a.nazivGrupe==g.naziv && !rez.contains(a))rez.add(a)
        rez=rez.sortedBy{it.datumPocetak}.toMutableList()
        return rez
    }

}