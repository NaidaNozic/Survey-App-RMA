package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Odgovor(
    @SerializedName("id")val id:Int,
    @SerializedName("odgovoreno")val odgovoreno:Int, //index odgovora
    @SerializedName("AnketaTakenId")val anketaTaken:Int=-1,
    @SerializedName("PitanjeId")val pitanjeId:Int=-1,
    @SerializedName("message")val message:String=" "
):Serializable