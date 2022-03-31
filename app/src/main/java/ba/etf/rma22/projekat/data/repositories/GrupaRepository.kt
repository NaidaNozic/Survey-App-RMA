package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.grupe

object GrupaRepository {
    fun getGroupsByIstrazivanjet(nazivIstrazivanja:String) : List<Grupa>{
        return grupe()
    }
}