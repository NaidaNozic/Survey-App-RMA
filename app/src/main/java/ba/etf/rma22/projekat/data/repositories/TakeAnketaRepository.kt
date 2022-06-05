package ba.etf.rma22.projekat.data.repositories

import android.util.Log
import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.models.AnketaTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

object TakeAnketaRepository {
   suspend fun zapocniAnketu(idAnkete:Int): AnketaTaken? {
        //kreira pokušaj za anketu,
        //vraća kreirani pokušaj ili null u slučaju greške
       var response = ApiAdapter.retrofit.zapocniOdgovaranje(AccountRepository.getHash(),idAnkete)
       val responseBody=response.body()
       if (response.isSuccessful && responseBody != null && responseBody.message==null) {
           return responseBody
       } else {
           Log.d("post Error-zapocniAnketu", "error ne mozee")
       }
       return null
    }

   suspend fun getPoceteAnkete():List<AnketaTaken>?{
        //vraća listu pokušaja ili null ukoliko student nema
        //niti jednu započetu anketu
       var response = ApiAdapter.retrofit.getPokusaje(AccountRepository.getHash())
       val responseBody=response.body()
       if (response.isSuccessful && responseBody != null && responseBody.size>=0) {
           return responseBody
       } else {
           Log.d("NEMA NITI JEDNE ZAPOCETE ANKETE", "Error")
       }
       return null
    }
}