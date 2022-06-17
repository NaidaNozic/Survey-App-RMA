package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Istrazivanje(
    @PrimaryKey  @SerializedName("id") val id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv")val naziv: String,
    @ColumnInfo(name = "godina") @SerializedName("godina") val godina: Int, //od 1 do 5
    @ColumnInfo(name = "upisana") var upisana:Int =0
):Serializable{
    override fun hashCode(): Int{
        return id.hashCode()
    }
    override fun equals(other: Any?): Boolean{
        val istrazivanje=other as Istrazivanje
        if(istrazivanje.id==this.id)return true
        return false
    }
}