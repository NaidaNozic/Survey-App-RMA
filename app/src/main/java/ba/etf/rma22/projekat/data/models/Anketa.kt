package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Anketa(
    @SerializedName("id") val id:Int,
    @SerializedName("naziv") val naziv: String,
    var nazivIstrazivanja: String=" ",
    @SerializedName("datumPocetak") val datumPocetak: Date,
    @SerializedName("datumKraj") var datumKraj: Date?,
    var datumRada: Date?=null,
    @SerializedName("trajanje") val trajanje: Int,
    @SerializedName("message")var message:String?=" ",
    var progres: Int=0,
    var nazivGrupe: String= " "
):Serializable{
    override fun hashCode(): Int{
        return id.hashCode()
    }
    override fun equals(other: Any?): Boolean{
        val anketa=other as Anketa
        if(anketa.id==this.id)return true
        return false
    }
}