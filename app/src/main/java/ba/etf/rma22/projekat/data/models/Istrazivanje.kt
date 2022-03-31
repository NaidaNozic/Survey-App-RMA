package ba.etf.rma22.projekat.data.models

import java.io.Serializable

data class Istrazivanje(
    val naziv: String,
    val godina: Int //od 1 do 5
):Serializable