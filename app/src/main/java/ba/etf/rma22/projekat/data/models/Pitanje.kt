package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Pitanje (
    @PrimaryKey  @SerializedName("id") val id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") val naziv: String,
    @ColumnInfo(name = "tekstPitanja") @SerializedName("tekstPitanja") val tekstPitanja: String,
    @ColumnInfo(name = "opcije") @SerializedName("opcije")val opcije:List<String> //ne moze lista u bazu. To provjeriti
):Serializable{
    override fun hashCode(): Int{
        return id.hashCode()
    }
    override fun equals(other: Any?): Boolean{
        val pitanje=other as Pitanje
        if(pitanje.id==this.id)return true
        return false
    }
}