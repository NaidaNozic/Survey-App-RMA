package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Istrazivanje(
    @SerializedName("id") val id: Int,
    @SerializedName("naziv")val naziv: String,
    @SerializedName("godina") val godina: Int //od 1 do 5
):Serializable