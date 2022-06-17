package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.data.AppDatabase1
import ba.etf.rma22.projekat.data.models.Account
import ba.etf.rma22.projekat.data.models.Anketa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AccountRepository {
    var acHash:String = "23e5b59e-996d-47d3-85c2-88651e9e95a2"
    suspend fun postaviHash(acHash:String):Boolean{
        //postavlja lokalno hash studenta koji će biti korišten u drugim repozitorijima, vraća true ukoliko je hash
        //postavljen, false inače.
        this.acHash=acHash
        var accounti=getAll(MainActivity.getContext())
        if(accounti!=null && accounti.size>0){
            Log.d("BRISE SE SVE","")
            deleteAll(MainActivity.getContext())
            Log.d("BROJ ACCOUNTA", getAll(MainActivity.getContext())?.size.toString())
            AnketaRepository.deleteAll(MainActivity.getContext())
            PitanjeAnketaRepository.deleteAll(MainActivity.getContext())
            OdgovorRepository.deleteAll(MainActivity.getContext())
            TakeAnketaRepository.deleteAll(MainActivity.getContext())
            PitanjeAnketaRepository.deleteAll(MainActivity.getContext())
            PitanjeAnketaRepository.deleteAllPitanja(MainActivity.getContext())
            AnketaIGrupeRepository.deleteAll(MainActivity.getContext())
            IstrazivanjeIGrupaRepository.deleteAll(MainActivity.getContext())
            IstrazivanjeIGrupaRepository.deleteAllGrupe(MainActivity.getContext())
        }
        writeAccount(MainActivity.getContext(),Account(0,acHash))
        return true
    }
    fun getHash():String{ //vraca hash studenta koji je postavljen
        return acHash
    }
    /////////BAZA METODE////////
    suspend fun getAll(context: Context) : List<Account>? { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.accountDao().getAll()
            return@withContext ankete
        }
    }
    suspend fun deleteAll(context: Context) : Int { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.accountDao().deleteAll()
            return@withContext ankete
        }
    }
    suspend fun writeAccount(context: Context,account:Account) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.accountDao().insertAll(account)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
}