package ba.etf.rma22.projekat.data.models

import java.util.*

data class PomocneAnkete(
    var idAnkete:Int,
    var nazivIstrazivanja:String?,
    var nazivGrupe:String?,
    var datumPredaje: Date?
)