package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.ankete
import ba.etf.rma22.projekat.data.models.Anketa

object AnketaRepository {
    fun getAnkete() : List<Anketa> {
        val ankete = ankete().sortedBy { it.datumPocetak }
        return ankete;
    }
}