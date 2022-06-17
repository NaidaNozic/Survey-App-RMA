package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["GrupaId", "AnketumId"])
data class AnketaIGrupe (
    @ColumnInfo(name = "GrupaId") @SerializedName("GrupaId") var GrupaId:Int,
    @ColumnInfo(name = "AnketumId") @SerializedName("AnketumId") var AnketumId:Int
)