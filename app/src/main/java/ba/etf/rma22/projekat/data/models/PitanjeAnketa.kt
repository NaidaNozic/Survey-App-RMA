package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["AnketumId", "PitanjeId"])
data class PitanjeAnketa (
    @ColumnInfo(name = "AnketumId") @SerializedName("AnketumId") var AnketumId:Int,
    @ColumnInfo(name = "PitanjeId") @SerializedName("PitanjeId") var PitanjeId:Int
){
}