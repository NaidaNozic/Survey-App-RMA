package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.grupe
import ba.etf.rma22.projekat.data.upisaneGrupe

object GrupaRepository {
   /* fun getGroupsByIstrazivanje(nazivIstrazivanja:String) : List<Grupa>{
        var grupe=grupe()
        var rez= mutableListOf<Grupa>()
        for(g in grupe)
            if(g.nazivIstrazivanja==nazivIstrazivanja)
                rez.add(g)
        return rez
    }*/
    fun getUpisani():List<Grupa>{
        return upisaneGrupe()
    }
}