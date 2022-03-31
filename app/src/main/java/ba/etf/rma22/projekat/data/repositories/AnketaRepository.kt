package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.ankete
import ba.etf.rma22.projekat.data.models.Anketa

object AnketaRepository {
    fun getAll() : List<Anketa> { //sve ankete
        val ankete = ankete().sortedBy { it.datumPocetak }
        return ankete;
    }
    fun getMyAnkete() : List<Anketa>{//sve moje ankete
        return ankete()
    }
    fun getDone() : List<Anketa>{ //uradjene ankete
        return ankete()
    }
    fun getFuture() : List<Anketa>{//buduce ankete
        return ankete()
    }
    fun getNotTaken() : List<Anketa>{//prosle(neuradjene) ankete
        return ankete()
    }
}