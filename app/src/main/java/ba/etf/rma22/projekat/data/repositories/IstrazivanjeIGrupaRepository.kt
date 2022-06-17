package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.AppDatabase1
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.InternetConnection
import ba.etf.rma22.projekat.data.models.Istrazivanje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object IstrazivanjeIGrupaRepository {

    suspend fun getIstrazivanja(offset:Int):List<Istrazivanje>?{
        //vraća sva istraživanja ili ako je zadan offset
        //odgovarajući page u rezultatima
        return withContext(Dispatchers.IO) {
            var responseBody:List<Istrazivanje>?
            if(InternetConnection.prisutna) {
                var response = ApiAdapter.retrofit.getIstrazivanja(offset)
                responseBody = response.body()
                if (responseBody != null) for(i in responseBody){
                    writeIstrazivanja(MainActivity.getContext(),
                        Istrazivanje(i.id,i.naziv,i.godina,0)
                    )
                }
            }else{
                responseBody=getIstrazivanjaByOffset(MainActivity.getContext(),(offset-1)*5)
                if(responseBody != null && responseBody.size>5){
                   var p=responseBody.toMutableList()
                    p.removeAll { i->i.id>(offset*5) }
                    responseBody=p
                }
            }
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
            var responseBody:List<Grupa>?
            if(InternetConnection.prisutna==true){
                responseBody= ApiAdapter.retrofit.getGrupe().body() //sve ankete
                if (responseBody != null) {
                    Log.d("VELICINA:",responseBody.size.toString())
                }
                var upisane= getUpisaneGrupe() //upisane
                if (responseBody != null) {
                    for(r in responseBody){
                        var i=0
                        if (upisane != null)if(upisane.find { g->g.id==r.id }!=null)i=1
                        writeGrupe(MainActivity.getContext(),Grupa(r.id,r.naziv,r.idIstrazivanja,i))
                    }
                }
            }else{
                responseBody=getAllGrupe(MainActivity.getContext())
                if (responseBody != null) {
                    Log.d("VELICINA-no internet:",responseBody.size.toString())
                }
            }
            if (responseBody != null) {
                Log.d("hhh",responseBody.size.toString())
            }
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
       // if(getAllGrupe(MainActivity.getContext()).find { g->g.id==idGrupa }!=null){
            postaviGrupuKaoUpisanu(MainActivity.getContext(),idGrupa)
      //  }
        val responseBody=response.body()
        if (response.isSuccessful && responseBody != null && responseBody.message.contains("je dodan u grupu")) {
            return true
        } else {
        }
        return false
    }
    suspend fun getUpisaneGrupe():List<Grupa>?{
        //vraca grupe u koje je student upisan
        return withContext(Dispatchers.IO) {
            var responseBody:List<Grupa>?
            if(InternetConnection.prisutna){
                var response = ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())
                responseBody = response.body()
                if(responseBody!=null)for(g in responseBody){
                    writeGrupe(MainActivity.getContext(),Grupa(g.id,g.naziv,g.idIstrazivanja,1))
                }
            }else{
                responseBody= getUpisaneGrupe(MainActivity.getContext())
            }
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
    ////////////ZA BAZU////////////
    suspend fun getAllIstrazivanja(context: Context) : List<Istrazivanje> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.istrazivanjeDao().getAllIstrazivanja()
            return@withContext ankete
        }
    }
    suspend fun getAllGrupe(context: Context) : List<Grupa> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.grupaDao().getAllGrupe()
            return@withContext ankete
        }
    }
    suspend fun getUpisaneGrupe(context: Context) : List<Grupa> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.grupaDao().getUpisaneGrupe()
            return@withContext ankete
        }
    }
    suspend fun deleteAll(context: Context) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.istrazivanjeDao().deleteAll()
            return@withContext ankete
        }
    }
    suspend fun deleteAllGrupe(context: Context) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.grupaDao().deleteAll()
            return@withContext ankete
        }
    }
    suspend fun postaviGrupuKaoUpisanu(context: Context,idGrupe:Int) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.grupaDao().postaviGrupuKaoUpisanu(idGrupe)
            return@withContext ankete
        }
    }
    suspend fun writeIstrazivanja(context: Context, istrazivanje:Istrazivanje) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.istrazivanjeDao().insertAll(istrazivanje)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun writeGrupe(context: Context, grupa:Grupa) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.grupaDao().insertAll(grupa)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
    suspend fun getIstrazivanjeById(context: Context,id: Int) : Istrazivanje { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.istrazivanjeDao().getIstrazivanjeById(id)
            return@withContext ankete
        }
    }
    suspend fun getIstrazivanjaByOffset(context: Context,offset: Int) : List<Istrazivanje>? { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.istrazivanjeDao().getIstrazivanjaByOffset(offset)
            return@withContext ankete
        }
    }
    suspend fun getIstrazivanjeByName(context: Context,name: String) : Istrazivanje { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.istrazivanjeDao().getIstrazivanjeByName(name)
            return@withContext ankete
        }
    }
    suspend fun getGrupaById(context: Context,id: Int) : Grupa { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.grupaDao().getGrupaById(id)
            return@withContext ankete
        }
    }
}