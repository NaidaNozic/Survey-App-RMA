package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.*
import java.util.*

class SveAnkete {
    companion object GlobalneAnkete{
        var upisanaIstrazivanja:MutableList<String> = mutableListOf()
        var upisaneGrupe:MutableList<String> = mutableListOf()

        var odgovoriPrijePredavanja:MutableMap<Int,Int> = mutableMapOf()
        var posljednjaOdabranaGodina:Int =1

        var svaIstrazivanja = mutableListOf<Istrazivanje>()
        var sveGrupe= mutableListOf<Grupa>()
    }
}