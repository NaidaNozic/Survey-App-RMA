package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.repositories.GrupaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeRepository
import java.io.Serializable

class Korisnik {

    companion object CompanionObject {
         var upisanaIstrazivanja = IstrazivanjeRepository.getUpisani().toMutableList()
         var upisaneGrupe = GrupaRepository.getUpisani().toMutableList()

         lateinit var posljednjeOdabranoIstrazivanje: Istrazivanje
         lateinit var posljednjeOdabranaGrupa: Grupa
         var posljednjaGodina: Int = 1
    }

    fun getPosljednjaGodina():Int{
        return posljednjaGodina
    }
    fun setposljednjaGodina(g:Int){
        posljednjaGodina=g
    }
    fun getUpisanaIstrazivanja():List<Istrazivanje>{
        return upisanaIstrazivanja
    }
    fun setPosljednjeOdabranoIstrazivanje(i:Istrazivanje){
        posljednjeOdabranoIstrazivanje=i
    }
    fun setPosljednjeOdabranaGrupa(g:Grupa){
        posljednjeOdabranaGrupa=g
    }
    fun getPosljednjeOdabranoIstrazivanje():String{
        return posljednjeOdabranoIstrazivanje.naziv
    }
    fun getPosljednjeOdabranaGrupa():String{
        return posljednjeOdabranaGrupa.naziv
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
    fun getNeupisanaIstrazivanja1():List<Istrazivanje>{
        var neupisane=mutableListOf<Istrazivanje>()
        var upisane=upisanaIstrazivanja
        var svi= IstrazivanjeRepository.getAll()
        for(n in svi)
            if(!upisane.any { u->u.naziv==n.naziv && u.godina==n.godina})
                neupisane.add(n)
        return neupisane
    }
    fun getGrupePoNeupisanimIstrazivanjima():List<String>{
        var rez= listOf<Grupa>()
        var neupisanaIstrazivanja=getNeupisanaIstrazivanja()
       /* for(n in neupisanaIstrazivanja){
            rez=rez.union(GrupaRepository.getGroupsByIstrazivanje(n)).toList()
        }*/
        var rez1= rez.map { r->r.naziv }
        return rez1
    }
    fun getGrupePoNeupisanimIstrazivanjima1():List<Grupa>{
        var rez= listOf<Grupa>()
        var neupisanaIstrazivanja=getNeupisanaIstrazivanja()
       /* for(n in neupisanaIstrazivanja){
            rez=rez.union(GrupaRepository.getGroupsByIstrazivanje(n)).toList()
        }*/
        return rez
    }
    fun addIstrazivanja(i:Istrazivanje){
        upisanaIstrazivanja.add(i)
    }
    fun addGrupu(g:Grupa){
        upisaneGrupe.add(g)
    }
}
