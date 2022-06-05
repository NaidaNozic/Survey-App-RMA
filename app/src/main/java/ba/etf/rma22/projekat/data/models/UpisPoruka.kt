package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpisPoruka(
    @SerializedName("message")val message: String
):Serializable