package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class AnketaTaken(
    @SerializedName("id") val id:Int,
    @SerializedName("student")val student:String,
    @SerializedName("progres")val progres:Int, //procenat odgovorenih pitanja od 0 do 100
    @SerializedName("datumRada")val datumRada: Date,
    @SerializedName("AnketumId")val AnketumId: Int=-1,
    @SerializedName("message")val message:String=" "
):Serializable