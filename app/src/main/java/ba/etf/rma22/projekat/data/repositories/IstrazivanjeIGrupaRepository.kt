package ba.etf.rma22.projekat.data.repositories

import android.util.Log
import android.widget.Toast
import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.viewmodel.IstrazivanjeIGrupaViewModel
import ba.etf.rma22.projekat.viewmodel.PitanjaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object IstrazivanjeIGrupaRepository {

    suspend fun getIstrazivanja(offset:Int):List<Istrazivanje>?{
        //vraća sva istraživanja ili ako je zadan offset
        //odgovarajući page u rezultatima
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getIstrazivanja(offset)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }
    suspend fun getIstrazivanja():List<Istrazivanje>?{
        var a: MutableList<Istrazivanje> = mutableListOf()
        var a1: MutableList<Istrazivanje>?
        var i =1
        while(1==1){
            a1= getIstrazivanja(i)?.toMutableList()
            if(a1?.size!=0 || a1==null) {
                a1?.let { a.addAll(it) }
                i++
            }else break
        }
        return a
    }
    suspend fun getGrupe():List<Grupa>?{
        //vraća sve grupe
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getGrupe()
            val responseBody = response.body()
            return@withContext responseBody
        }
    }
    suspend fun getIstrazivanje(id: Int):Istrazivanje?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getIstrazivanje(id)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }
    suspend fun upisiUGrupu(idGrupa:Int):Boolean{
        //upisuje studenta u grupu sa id-em idGrupa i vraća
        //true ili vraća false ako nije moguće upisati studenta
        var response = ApiAdapter.retrofit.upisiUGrupu(idGrupa,AccountRepository.acHash)
        val responseBody=response.body()
        if (response.isSuccessful && responseBody != null && responseBody.message.contains("je dodan u grupu")) {
            Log.d("post response", responseBody.message)
            return true
        } else {
            Log.d("post Error", "error ne mozee")
        }
        return false
    }
    suspend fun getUpisaneGrupe(hashStudenta:String):List<Grupa>?{
        //vraca grupe u koje je student upisan
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getUpisaneGrupe(hashStudenta)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }
    suspend fun getIstrazivanjeGrupe(idGrupe:Int):Istrazivanje?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getIstrazivanjeGrupe(idGrupe)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }
}