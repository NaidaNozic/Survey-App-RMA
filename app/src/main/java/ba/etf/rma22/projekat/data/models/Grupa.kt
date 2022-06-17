package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Grupa (
    @PrimaryKey @SerializedName("id")val id:Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv")val naziv: String,
    @ColumnInfo(name = "IstrazivanjeId") @SerializedName("IstrazivanjeId")val idIstrazivanja: Int,
    @ColumnInfo(name = "upisana") var upisana:Int =0
):Serializable{
    override fun hashCode(): Int{
        return id.hashCode()
    }
    override fun equals(other: Any?): Boolean{
        val grupa=other as Grupa
        if(grupa.id==this.id)return true
        return false
    }
}