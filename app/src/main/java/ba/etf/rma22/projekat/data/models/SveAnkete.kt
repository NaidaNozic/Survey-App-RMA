package ba.etf.rma22.projekat.data.models

import ba.etf.rma22.projekat.data.*
import java.util.*

class SveAnkete {
    companion object GlobalneAnkete{
        var upisanaIstrazivanja:MutableList<String> = mutableListOf()
        var upisaneGrupe:MutableList<String> = mutableListOf()
        //ove ankete su globalne i mogu im atribute mijenjati
        var ankete= ankete().toMutableList()
    }
}