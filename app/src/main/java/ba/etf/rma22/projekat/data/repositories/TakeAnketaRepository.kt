package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.AppDatabase1
import ba.etf.rma22.projekat.data.models.AnketaTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TakeAnketaRepository {
   suspend fun zapocniAnketu(idAnkete:Int): AnketaTaken? {
        //kreira pokušaj za anketu,
        //vraća kreirani pokušaj ili null u slučaju greške
       return withContext(Dispatchers.IO) {
           var response = ApiAdapter.retrofit.zapocniOdgovaranje(AccountRepository.getHash(),idAnkete)
           val responseBody=response.body()
           if (response.isSuccessful && responseBody != null && (responseBody.message==null || responseBody.message==" ")) {
               return@withContext responseBody
           } else {
           }
           return@withContext null
       }
    }

   suspend fun getPoceteAnkete():List<AnketaTaken>?{
        //vraća listu pokušaja ili null ukoliko student nema
        //niti jednu započetu anketu
       return withContext(Dispatchers.IO) {
           var response = ApiAdapter.retrofit.getPokusaje(AccountRepository.getHash())
           val responseBody=response.body()
           if (response.isSuccessful && responseBody != null && responseBody.size!=null && responseBody.size>0) {
               return@withContext responseBody
           }
           return@withContext null
       }
   }
    /////////METODE ZA BAZUU///////////
    suspend fun getAll(context: Context) : List<AnketaTaken> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaTakenDao().getAll()
            return@withContext ankete
        }
    }
    suspend fun updateProgres(context: Context,progres:Int,idAnketaTaken:Int) : Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaTakenDao().updateProgres(progres,idAnketaTaken)
            return@withContext ankete
        }
    }
    suspend fun getAnketaTakenByIdAnkete(context: Context,idAnkete:Int) : AnketaTaken? { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaTakenDao().getAnketaTakenByIdAnkete(idAnkete)
            return@withContext ankete
        }
    }
    suspend fun writeTakenAnkete(context: Context, anketataken: AnketaTaken) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.anketaTakenDao().insertAll(anketataken)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun deleteAll(context: Context) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaTakenDao().deleteAll()
            return@withContext ankete
        }
    }
}