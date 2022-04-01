package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import java.io.Serializable

class Korisnik:Serializable {
    private var upisanaIstrazivanja=mutableListOf<Istrazivanje>()
    private var upisaneGrupe= mutableListOf<Grupa>()

    fun getUpisanaIstrazivanja():List<Istrazivanje>{
        return upisanaIstrazivanja
    }
    fun getupisaneGrupe():List<Grupa>{
        return upisaneGrupe
    }
    fun setUpisane(lista:List<Istrazivanje>){
        for(i in lista)
                upisanaIstrazivanja.add(i)
    }
    fun setupisaneGrupe(lista:List<Grupa>){
        for(i in lista)
            upisaneGrupe.add(i)
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
    fun getGrupePoNeupisanimIstrazivanjima():List<String>{
        var rez= listOf<Grupa>()
        var neupisanaIstrazivanja=getNeupisanaIstrazivanja()
        for(n in neupisanaIstrazivanja){
            rez=rez.union(GrupaRepository.getGroupsByIstrazivanje(n)).toList()
        }
        var rez1= rez.map { r->r.naziv }
        return rez1
    }
    fun addIstrazivanja(i:Istrazivanje){
        upisanaIstrazivanja.add(i)
    }
    fun addGrupu(g:Grupa){
        upisaneGrupe.add(g)
    }
}