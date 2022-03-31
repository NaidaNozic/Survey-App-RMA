package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.istrazivanja
import ba.etf.rma22.projekat.data.upisanaIstrazivanja

object IstrazivanjeRepository {
    fun getIstrazivanjeByGodina(godina:Int) : List<Istrazivanje>{ // lista istraživanja na godini
        var svi= getAll()
        var rez= mutableListOf<Istrazivanje>()
        for(i in svi)
            if(i.godina==godina)
                rez.add(i)
        return rez
    }
    fun getAll() : List<Istrazivanje>{ //lista svih istraživanja
        return istrazivanja()
    }
    fun getUpisani() : List<Istrazivanje>{//lista istraživanja na kojima je korisnik upisan
        return upisanaIstrazivanja()
    }
}