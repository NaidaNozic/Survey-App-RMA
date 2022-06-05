package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Grupa (
    @SerializedName("id")val id:Int,
    @SerializedName("naziv")val naziv: String,
    @SerializedName("IstrazivanjeId")val idIstrazivanja: Int
):Serializable