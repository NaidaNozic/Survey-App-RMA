package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Grupa (
    @SerializedName("id")val id:Int,
    @SerializedName("naziv")val naziv: String,
    @SerializedName("IstrazivanjeId")val idIstrazivanja: Int
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