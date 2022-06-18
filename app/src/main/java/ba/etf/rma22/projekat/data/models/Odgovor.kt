package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Odgovor(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val id:Int,
    @ColumnInfo(name = "odgovoreno") @SerializedName("odgovoreno")val odgovoreno:Int, //index odgovora
): Serializable