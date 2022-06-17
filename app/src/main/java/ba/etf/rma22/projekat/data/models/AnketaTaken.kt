package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity
data class AnketaTaken(
    @PrimaryKey @SerializedName("id") val id:Int,
    @ColumnInfo(name = "student") @SerializedName("student")val student:String,
    @ColumnInfo(name = "progres") @SerializedName("progres")val progres:Int, //procenat odgovorenih pitanja od 0 do 100
    @ColumnInfo(name = "datumRada") @SerializedName("datumRada")val datumRada: Date,
    @ColumnInfo(name = "AnketumId") @SerializedName("AnketumId")val AnketumId: Int=-1,
    @SerializedName("message")val message:String=" "
):Serializable{
    override fun hashCode(): Int{
        return id.hashCode()
    }
    override fun equals(other: Any?): Boolean{
        val anketaTaken=other as AnketaTaken
        if(anketaTaken.id==this.id)return true
        return false
    }
}