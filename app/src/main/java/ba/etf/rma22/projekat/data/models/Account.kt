package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Account (
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val id:Int=1,
    @ColumnInfo(name = "acHash") @SerializedName("acHash")  var acHash :String
)
