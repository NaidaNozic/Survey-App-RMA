package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pitanje (
    @SerializedName("id") val id: Int,
    @SerializedName("naziv") val naziv: String,
    @SerializedName("tekstPitanja") val tekstPitanja: String,
    @SerializedName("opcije")val opcije:List<String>
):Serializable