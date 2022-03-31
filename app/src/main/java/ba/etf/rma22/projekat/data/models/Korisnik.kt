package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import java.io.Serializable

class Korisnik:Serializable {
    private var upisanaIstrazivanja=mutableListOf<Istrazivanje>()
    //IstrazivanjeRepository.getUpisani()as MutableList<Istrazivanje>

    fun getUpisanaIstrazivanja():List<Istrazivanje>{
        return upisanaIstrazivanja
    }
    fun setUpisane(lista:List<Istrazivanje>){
        for(i in lista)
                upisanaIstrazivanja.add(i)
    }
    fun getNeupisanaIstrazivanja():List<String>{
        var neupisane=mutableListOf<String>()
        var upisane=upisanaIstrazivanja
        var svi= IstrazivanjeRepository.getAll()
        for(n in svi)
            if(!upisane.any { u->u.naziv==n.naziv && u.godina==n.godina})
                neupisane.add(n.naziv)
        return neupisane
    }
    fun addIstrazivanja(i:Istrazivanje){
        upisanaIstrazivanja.add(i)
    }
}