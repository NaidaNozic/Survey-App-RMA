package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.AppDatabase1
import ba.etf.rma22.projekat.data.models.AnketaIGrupe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AnketaIGrupeRepository {
    suspend fun getAll(context: Context) : List<AnketaIGrupe> {
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaIGrupeDao().getAll()
            return@withContext ankete
        }
    }
    suspend fun getAnketaGrupeByIdAnkete(context: Context,idAnkete:Int) : List<AnketaIGrupe> {
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaIGrupeDao().getAnketaGrupeByIdAnkete(idAnkete)
            return@withContext ankete
        }
    }
    suspend fun getAnketaGrupeByIdGrupe(context: Context,idGrupe:Int) : List<AnketaIGrupe> {
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaIGrupeDao().getAnketaGrupeByIdGrupe(idGrupe)
            return@withContext ankete
        }
    }
    suspend fun deleteAll(context: Context) :Int{
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaIGrupeDao().deleteAll()
            return@withContext ankete
        }
    }
    suspend fun writeAnkete(context: Context, anketa: AnketaIGrupe) : String?{
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.anketaIGrupeDao().insertAll(anketa)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
}