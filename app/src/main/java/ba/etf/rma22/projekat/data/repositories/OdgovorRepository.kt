package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.AppDatabase1
import ba.etf.rma22.projekat.data.models.JsonZaOdgovor
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.Odgovor1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OdgovorRepository {

   suspend fun getOdgovoriAnketa(idAnkete:Int):List<Odgovor1>?{
        //vraća listu odgovora za anketu,
        //praznu listu ako student nije odgovarao ili nije upisan na zadanu anketu
       return withContext(Dispatchers.IO) {
           var poceta=TakeAnketaRepository.getPoceteAnkete()?.find { a->a.AnketumId==idAnkete }
           var response = poceta?.let { ApiAdapter.retrofit.getOdgovori(AccountRepository.getHash(), it.id) }
           val responseBody = response?.body()
           if(responseBody != null && responseBody.size==0)return@withContext listOf()
           return@withContext responseBody
       }
    }
    suspend fun postaviOdgovorAnketa(idAnketaTaken:Int,idPitanje:Int,odgovor:Int):Int?{
        //Funkcija postavlja odgovor sa indeksom odgovor na pitanje sa id-em idPitanje u okviru pokušaja sa id-em idAnketaTaken.
        //Funkcija vraća progres (0-100) na anketi nakon odgovora ili -1 ukoliko ima neka greška u zahtjevu
        return withContext(Dispatchers.IO) {
            var pocete=TakeAnketaRepository.getPoceteAnkete()
            var idAnkete=-1
            if(pocete!=null){
                idAnkete= pocete.find { p->p.id==idAnketaTaken }?.AnketumId!!
            }
            var o= getOdgovoriAnketa(idAnkete)?.size?.plus(1)
            var o1:Int
            if (o!=null) o1=o
            else o1=0
            var brojPitanja= PitanjeAnketaRepository.getPitanja(idAnkete)?.size
            var json=JsonZaOdgovor(odgovor,idPitanje,zaokruziProgres(o1.toFloat()/brojPitanja!!))
            //obaviti update ankete taken u bazi
            TakeAnketaRepository.updateProgres(MainActivity.getContext(),zaokruziProgres(o1.toFloat()/brojPitanja!!),idAnketaTaken)
            //upisati odgovor u bazu
            writeOdgovore(MainActivity.getContext(), Odgovor1(odgovor,idAnketaTaken,idPitanje," "))
            writeOdgovore1(MainActivity.getContext(),Odgovor(0,odgovor))//ovo je samo radi testova dodano

            var response = ApiAdapter.retrofit.dodajOdgovor(AccountRepository.getHash(),idAnketaTaken,json)
            val responseBody=response.body()
            if (response.isSuccessful && responseBody != null /*&& responseBody.message==" "*/) {
                var anketeTaken=TakeAnketaRepository.getPoceteAnkete()
                if (anketeTaken != null)
                    return@withContext anketeTaken.find { a->a.id==idAnketaTaken }?.progres!!
            }
            return@withContext -1
        }
    }
    private fun zaokruziProgres(progres:Float):Int{
        var rez=progres
        if(progres>0F && progres<0.2){
            if(0F+0.1<progres) rez=0F else rez=0.2F
        }else if(progres>0.2F && progres<0.4F){
            if(0.2+0.1<progres) rez=0.2F else rez=0.4F
        }else if(progres>0.4F && progres<0.6F){
            if(0.4F+0.1<progres) rez=0.4F else rez=0.6F
        }else if(progres>0.6F && progres<0.8F){
            if(0.6F+0.1F<progres) rez=0.6F else rez=0.8F
        }else if(progres>80 && progres<1F){
            if(0.8F+0.1F<progres) rez=0.8F else rez=1F
        }
        return (rez*100).toInt()
    }
    //////////METODE ZA BAZU//////////
    suspend fun getAll(context: Context) : List<Odgovor1> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.odgovor1DAO().getAll()
            return@withContext ankete
        }
    }
    suspend fun getOdgovoriAnketa(context: Context,idAnketaTaken:Int) : List<Odgovor1> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.odgovor1DAO().findOdgovoreAnkete(idAnketaTaken)
            return@withContext ankete
        }
    }
    suspend fun writeOdgovore(context: Context, odgovor1: Odgovor1) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.odgovor1DAO().insertAll(odgovor1)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun writeOdgovore1(context: Context, odgovor: Odgovor) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.odgovorDao().insertAll(odgovor)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun getAll1(context: Context) : List<Odgovor> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.odgovorDao().getAll()
            return@withContext ankete
        }
    }
    suspend fun deleteAll(context: Context) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.odgovor1DAO().deleteAll()
            return@withContext ankete
        }
    }
}