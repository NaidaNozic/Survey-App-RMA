package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity
data class Anketa(
    @PrimaryKey @SerializedName("id") val id:Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") val naziv: String,
    var nazivIstrazivanja: String=" ",
    @ColumnInfo(name = "datumPocetak") @SerializedName("datumPocetak") val datumPocetak: Date,
    @ColumnInfo(name = "datumKraj") @SerializedName("datumKraj") var datumKraj: Date?,
    var datumRada: Date?=null,
    @ColumnInfo(name = "trajanje") @SerializedName("trajanje") val trajanje: Int,
    @SerializedName("message")var message:String?=" ",
    var progres: Int=0,
    var nazivGrupe: String= " ",
    @ColumnInfo(name = "upisana") @SerializedName("upisana") val upisana: Int,
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