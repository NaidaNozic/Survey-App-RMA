package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository

class KorisnikListViewModel {
    fun getIstrazivanjeByGodina(godina:Int) : List<Istrazivanje>{ // lista istraživanja na godini
        return IstrazivanjeRepository.getIstrazivanjeByGodina(godina)
    }
    fun getAll() :List<Istrazivanje>{
        return IstrazivanjeRepository.getAll()
    }
    fun getUpisani():List<Istrazivanje>{
        return IstrazivanjeRepository.getUpisani()
    }
    fun getNeupisane():List<String>{
        var neupisane:MutableList<String>
        neupisane= mutableListOf()
        var upisane=IstrazivanjeRepository.getUpisani()
        var svi=IstrazivanjeRepository.getAll()
        for(n in svi)
            if(!upisane.any { u->u.naziv==n.naziv && u.godina==n.godina})
                neupisane.add(n.naziv)
        return neupisane
    }
    fun getNeupisane(n1:String):List<String>{
        var neupisane:MutableList<String>
        neupisane= mutableListOf()
        var upisane=IstrazivanjeRepository.getUpisani()
        var svi=IstrazivanjeRepository.getAll()
        for(n in svi)
            if(!upisane.any { u->u.naziv==n.naziv && u.godina==n.godina} && n.naziv!=n1)
                neupisane.add(n.naziv)
        return neupisane
    }
}
