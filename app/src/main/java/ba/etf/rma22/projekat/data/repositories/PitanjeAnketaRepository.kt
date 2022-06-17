package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.AppDatabase1
import ba.etf.rma22.projekat.data.models.InternetConnection
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.PitanjeAnketa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PitanjeAnketaRepository {
    suspend fun getPitanja(idAnkete: Int): List<Pitanje>? {
        //vraća sva pitanja na anketi sa zadanim id-em
        return withContext(Dispatchers.IO) {
            var responseBody:List<Pitanje>?
            if(InternetConnection.prisutna){
                var response = ApiAdapter.retrofit.getPitanja(idAnkete)
                responseBody = response.body()
                if(responseBody!=null)for(p in responseBody) {
                    writePitanja(MainActivity.getContext(),Pitanje(p.id, p.naziv, p.tekstPitanja, p.opcije))
                    writePitanjeAnketa(MainActivity.getContext(), PitanjeAnketa(idAnkete,p.id))
                }
            }else{
                var mut= mutableListOf<Pitanje>()
                var aa=getAllPitanjeAnketaById(MainActivity.getContext(),idAnkete)
                if(aa!=null)for(l in aa){
                    mut.add(getPitanjaById(MainActivity.getContext(),l.PitanjeId))
                }
                responseBody=mut
            }
            return@withContext responseBody
        }
    }
    ///////////////////METODE ZA BAZU///////////////////////
    suspend fun getAll(context: Context) : List<Pitanje> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.pitanjeDao().getAll()
            return@withContext ankete
        }
    }
    suspend fun writePitanja(context: Context, pitanje: Pitanje) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.pitanjeDao().insertAll(pitanje)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun getPitanjaById(context: Context,pitanjeId: Int) : Pitanje { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.pitanjeDao().getPitanjaById(pitanjeId)
            return@withContext ankete
        }
    }
    ///////////////////PITANJE ANKETA///////////////////////
    suspend fun getAllPitanjeAnketa(context: Context) : List<PitanjeAnketa> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.pitanjeAnketaDao().getAllPitanjeAnketa()
            return@withContext ankete
        }
    }
    suspend fun writePitanjeAnketa(context: Context,pitanjeAnkete: PitanjeAnketa) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.pitanjeAnketaDao().insertAll(pitanjeAnkete)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun getAllPitanjeAnketaById(context: Context,idAnkete: Int) : List<PitanjeAnketa> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.pitanjeAnketaDao().getAllPitanjeAnketaByIdAnkete(idAnkete)
            return@withContext ankete
        }
    }
    suspend fun deleteAll(context: Context) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.pitanjeAnketaDao().deleteAll()
            return@withContext ankete
        }
    }
    suspend fun deleteAllPitanja(context: Context) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.pitanjeDao().deleteAll()
            return@withContext ankete
        }
    }
}