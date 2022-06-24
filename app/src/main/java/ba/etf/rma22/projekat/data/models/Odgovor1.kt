package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(primaryKeys = ["AnketaTakenId", "PitanjeId"])
data class Odgovor1(
    @ColumnInfo(name = "odgovoreno") @SerializedName("odgovoreno")val odgovoreno:Int, //index odgovora
    @ColumnInfo(name = "AnketaTakenId") @SerializedName("AnketaTakenId")val anketaTaken:Int=-1,
    @ColumnInfo(name = "PitanjeId")@SerializedName("PitanjeId")val pitanjeId:Int=-1,
    @SerializedName("message")val message:String=" "
):Serializable