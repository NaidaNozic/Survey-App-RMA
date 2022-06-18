package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.AppDatabase1
import ba.etf.rma22.projekat.data.models.*
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.*

object AnketaRepository {

    suspend fun getAll() : List<Anketa> { //sve ankete
        var a: MutableList<Anketa> =mutableListOf()
        var a1: MutableList<Anketa>?
        var i =1
        while(i>=0){
            a1= getAll(i)?.toMutableList()
            if(a1?.size!=0) {
                a1?.let { a.addAll(it) }
                i++
            }else break
        }
        if (a != null) {
            val iterator = a.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (a.count { a ->a.naziv == item.naziv && a.nazivIstrazivanja == item.nazivIstrazivanja} > 1) iterator.remove()
            }
        }
        val result: List<Anketa> =a
        return result.sortedBy{it.datumPocetak}
    }
    suspend fun getNotTaken(): List<Anketa> {
        var rjesenje: MutableList<Anketa> = mutableListOf()
        var progres:Int?=0
        for (s in getAll()!!) {
            if(InternetConnection.prisutna){
                val pocete=TakeAnketaRepository.getPoceteAnkete()
                if (pocete != null) {
                    progres= pocete.find { p->p.AnketumId==s.id }?.progres
                }
            }else{
                progres= TakeAnketaRepository.getAnketaTakenByIdAnkete(MainActivity.getContext(),s.id)?.progres
            }
            if(progres!=null)
            if (((s.datumKraj != null && s.datumKraj!! < Date())) && progres < 100)
                rjesenje.add(s)
        }
        return rjesenje
    }

    suspend fun getFuture(): List<Anketa> {
        var rjesenje: MutableList<Anketa> = mutableListOf()
        for (s in getAll()!!) {
            if (s.datumPocetak > Date())
                rjesenje.add(s)
        }
        return rjesenje
    }

    suspend fun getDone(): List<Anketa>? {
        return withContext(Dispatchers.IO) {
            var response = getUpisane()
            var result: MutableList<Anketa> = mutableListOf()
            var brPitanja:Int =0
            var odgovori: List<Odgovor1>
            if (response != null)
                for (r in response) {
                    if(InternetConnection.prisutna) {
                        var poceteAnkete = TakeAnketaRepository.getPoceteAnkete()!!
                        var pocetaAnketa = poceteAnkete.find { p -> p.AnketumId == r.id }
                        for (i in 0..poceteAnkete.size-1) {
                            TakeAnketaRepository.writeTakenAnkete(
                                MainActivity.getContext(),
                                AnketaTaken(
                                    poceteAnkete[i].id,
                                    poceteAnkete[i].student,
                                    poceteAnkete[i].progres,
                                    poceteAnkete[i].datumRada,
                                    poceteAnkete[i].AnketumId,
                                    " "
                                )
                            )
                        }
                        if (pocetaAnketa != null) {
                            odgovori =pocetaAnketa?.let { OdgovorRepository.getOdgovoriAnketa(it.AnketumId) }!!
                            for (o in odgovori)
                                OdgovorRepository.writeOdgovore(
                                    MainActivity.getContext(),
                                    Odgovor1(o.odgovoreno, o.anketaTaken, o.pitanjeId, " ")
                                )
                            //odgovori=OdgovorRepository.getOdgovoriAnketa(MainActivity.getContext(),pocetaAnketa.id)
                        } else odgovori = listOf()
                    }else {
                        var pocetaAnketa = TakeAnketaRepository.getAnketaTakenByIdAnkete(MainActivity.getContext(),r.id)
                        if (pocetaAnketa != null) {
                            odgovori = OdgovorRepository.getOdgovoriAnketa(MainActivity.getContext(),pocetaAnketa.id)
                        } else odgovori = listOf()
                    }
                    brPitanja = PitanjeAnketaRepository.getPitanja(r.id)?.size ?: 0
                    if (brPitanja == odgovori.size && brPitanja > 0 && (r.datumKraj == null || r.datumKraj!! > Date()) && r.datumPocetak < Date()) {
                        result.add(r)
                    }

                }
            if(result==null)return@withContext listOf<Anketa>()
            return@withContext result
        }
    }



    suspend fun getAll(offset: Int): List<Anketa>? {
        //vraća listu svih anketa ili ako je zadan offset
        //odgovarajući page u rezultatima (npr ako je pozvana metoda bez parametra vraćaju se
        //sve ankete, a ako je offset 1 vraća se samo prvih 5)
        var allAnkete: MutableList<Anketa> = mutableListOf()
        var responseBody:MutableList<Anketa> = mutableListOf()

        return withContext(Dispatchers.IO) {
            if(InternetConnection.prisutna){
                var response = ApiAdapter.retrofit.getAnkete(offset)
                responseBody = response.body()!!
                for(a in responseBody) writeAnkete(MainActivity.getContext(),
                                       Anketa(a.id,a.naziv," ",a.datumPocetak,a.datumKraj,a.datumRada,
                                              a.trajanje,"",a.progres," ",0))
            }else{
                var tmp=getAnketeByOffset(MainActivity.getContext(),(offset-1)*5).toMutableList()
                if(tmp != null && tmp.size>5){
                    tmp.removeAll { i->i.id>(offset*5) }
                }
                responseBody=tmp
            }
            if (responseBody != null)
                for (a in responseBody) {
                    var grupe: List<Grupa>?
                    if (InternetConnection.prisutna) {
                        grupe = ApiAdapter.retrofit.getGrupeAnkete(a.id).body()
                        if (grupe != null) {
                            for (g in grupe) {
                                IstrazivanjeIGrupaRepository.writeGrupe(
                                    MainActivity.getContext(),
                                    Grupa(g.id, g.naziv, g.idIstrazivanja, 0)
                                )
                                AnketaIGrupeRepository.writeAnkete(
                                    MainActivity.getContext(),
                                    AnketaIGrupe(g.id, a.id)
                                )
                            }
                        }
                    } else {
                        var ag = AnketaIGrupeRepository.getAnketaGrupeByIdAnkete(MainActivity.getContext(),a.id)
                        var pom = mutableListOf<Grupa>()
                        for (q in ag) {
                            pom.add(
                                IstrazivanjeIGrupaRepository.getGrupaById(
                                    MainActivity.getContext(),
                                    q.GrupaId
                                )
                            )
                        }
                        grupe = pom
                    }
                    var istrazivanja:List<String?>?
                    if (InternetConnection.prisutna) {
                        istrazivanja =grupe?.map { g -> IstrazivanjeIGrupaRepository.getIstrazivanje(g.idIstrazivanja)?.naziv }?.distinct()
                        var pop=grupe?.map { g -> IstrazivanjeIGrupaRepository.getIstrazivanje(g.idIstrazivanja)}
                        if (pop != null) {
                            for(ik in pop){
                                if (ik != null) {
                                    IstrazivanjeIGrupaRepository.writeIstrazivanja(MainActivity.getContext(),
                                    Istrazivanje(ik.id,ik.naziv,ik.godina,0)
                                    )
                                }
                            }
                        }
                    }else{
                        istrazivanja=grupe?.map{g->IstrazivanjeIGrupaRepository.getIstrazivanjeById(MainActivity.getContext(),g.idIstrazivanja)?.naziv}?.distinct()
                    }
                    if (istrazivanja != null)
                        for (i in istrazivanja) {
                            if (a.nazivIstrazivanja == null || a.nazivIstrazivanja == " ")
                                a.nazivIstrazivanja = i.toString()
                            else a.nazivIstrazivanja = a.nazivIstrazivanja + "," + i.toString()
                        }
                    allAnkete.add(a)
                }
            allAnkete=dodijeliProgres1(allAnkete)
            if (allAnkete != null) {
                val iterator = allAnkete.iterator()
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    if (allAnkete.count { a ->a.naziv == item.naziv && a.nazivIstrazivanja == item.nazivIstrazivanja} > 1) iterator.remove()
                }
            }
            return@withContext allAnkete.sortedBy { it.datumPocetak }
        }
    }

    suspend fun getById(id: Int): Anketa? {//radi
        //vraća jednu anketu koja ima zadani id ili null ako anketa ne postoji
        return withContext(Dispatchers.IO) {
            var responseBody:Anketa?
            if(InternetConnection.prisutna){
                var response = ApiAdapter.retrofit.getAnketaById(id)
                responseBody = response.body()
                if(responseBody!=null)
                writeAnkete(MainActivity.getContext(),Anketa(id,responseBody.naziv,"",responseBody.datumPocetak,
                           responseBody.datumKraj,null,responseBody.trajanje,"",0,"",0))
            }else{
                responseBody= getAnketaById(MainActivity.getContext(),id)
            }
            if (responseBody != null && responseBody.message == "Anketa not found.") return@withContext null
            return@withContext responseBody
        }
    }

    suspend fun getUpisane(): List<Anketa>? {
        return withContext(Dispatchers.IO) {
            var response: Response<List<Anketa>>
            var myAnkete: MutableList<Anketa>? = mutableListOf()

            var upisaneGrupe= listOf<Grupa>()
            if(InternetConnection.prisutna) {
                upisaneGrupe = IstrazivanjeIGrupaRepository.getUpisaneGrupe()!!
                for(u in upisaneGrupe)IstrazivanjeIGrupaRepository.writeGrupe(MainActivity.getContext(),
                    Grupa(u.id,u.naziv,u.idIstrazivanja,1)
                )
            }
            else upisaneGrupe=IstrazivanjeIGrupaRepository.getUpisaneGrupe(MainActivity.getContext())

            for (g in upisaneGrupe) {
                var responseBody = listOf<Anketa>() //lista anketa koja ce se vratiti
                if(InternetConnection.prisutna) {
                    response = ApiAdapter.retrofit.getAnketeGrupe(g.id)
                    responseBody= response.body()!!
                    for(r in responseBody){
                        AnketaIGrupeRepository.writeAnkete(MainActivity.getContext(),
                            AnketaIGrupe(g.id,r.id)
                        )
                    }
                }else{
                    var pom1=AnketaIGrupeRepository.getAnketaGrupeByIdGrupe(MainActivity.getContext(),g.id)
                    var tmp= mutableListOf<Anketa>()
                    for(p in pom1) tmp.add(getAnketaById(MainActivity.getContext(),p.AnketumId))
                    Log.d("TMP:",tmp.size.toString())
                    responseBody=tmp
                }
                //naci istrazivanje za ovu grupu
                var i:Istrazivanje?
                if(InternetConnection.prisutna){
                    i = IstrazivanjeIGrupaRepository.getIstrazivanje(g.idIstrazivanja)
                    if (i != null) {
                        IstrazivanjeIGrupaRepository.writeIstrazivanja(MainActivity.getContext(),Istrazivanje(i.id,i.naziv,i.godina,1)
                        )
                    }
                }else{
                    i=IstrazivanjeIGrupaRepository.getIstrazivanjeById(MainActivity.getContext(),g.idIstrazivanja)
                }
                //dodijeliti tim anketama naziv istrazivanja
                if (myAnkete != null) {
                    responseBody.map { a->a.nazivGrupe=g.naziv }
                    responseBody.map { a-> if (i != null) { a.nazivIstrazivanja=i.naziv } }
                    myAnkete.addAll(responseBody)
                }
            }
            if (myAnkete != null) myAnkete = myAnkete.sortedBy { it.datumPocetak }.toMutableList()
            if (myAnkete != null) if(myAnkete.size==0) {
                return@withContext null
            }

            if (myAnkete != null)
                myAnkete.forEach { a->writeAnkete(MainActivity.getContext(),Anketa(a.id,a.naziv," ",a.datumPocetak,
                                   a.datumKraj,null,a.trajanje," ",0," ",1)) }
            //dodijelitiProgres(myAnkete)
            return@withContext myAnkete
        }
    }
    suspend fun dodijeliProgres1(ankete:MutableList<Anketa>): MutableList<Anketa> {
        if (ankete != null) {
            for(a in ankete){
                var poceteAnkete:List<AnketaTaken>? =listOf<AnketaTaken>()
                if(InternetConnection.prisutna) {
                    poceteAnkete = TakeAnketaRepository.getPoceteAnkete()
                }
                if(poceteAnkete==null){
                    a.progres=0
                }
                else{
                    var pocetaAnketa = poceteAnkete.find { p -> p.AnketumId == a.id }
                    if (pocetaAnketa != null) a.progres=pocetaAnketa.progres
                }
            }
        }
        return ankete
    }
    suspend fun dodijelitiProgres(ankete:MutableList<Anketa>?):MutableList<Anketa>?{
        if (ankete != null)
            for(a in ankete){
                var poceteAnkete:List<AnketaTaken>? = listOf()
                if(InternetConnection.prisutna){
                    poceteAnkete = TakeAnketaRepository.getPoceteAnkete()
                }else{
                    poceteAnkete=TakeAnketaRepository.getAll(MainActivity.getContext())
                }
                if(poceteAnkete==null){
                    a.progres=0
                }
                else {
                    var pocetaAnketa = poceteAnkete.find { p -> p.AnketumId == a.id }
                    val tmp: Int?
                    tmp = pocetaAnketa?.progres
                    if(tmp!=null && SveAnkete.upisanaIstrazivanja.find { i->i==a.nazivIstrazivanja }!=null
                        && SveAnkete.upisaneGrupe.find { g->g==a.nazivGrupe }!=null){
                        a.progres=tmp
                    }else if(tmp==null)a.progres=0
                }
            }
        return ankete
    }
    ///////////////////METODE ZA BAZU///////////////////////
    suspend fun getAll(context: Context) : List<Anketa> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaDao().getAll()
            return@withContext ankete
        }
    }
    suspend fun getAnketeByOffset(context: Context,offset: Int) : List<Anketa> { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaDao().getAnketeByOffset(offset)
            return@withContext ankete
        }
    }
    suspend fun getAnketaById(context: Context,AnketumId: Int) : Anketa { //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaDao().getAnketaById(AnketumId)
            return@withContext ankete
        }
    }
    suspend fun deleteAll(context: Context) :Int{ //dobavlja sve ankete iz baze
        return withContext(Dispatchers.IO) {
            var db = AppDatabase1.getInstance(context)
            var ankete = db!!.anketaDao().deleteAll()
            return@withContext ankete
        }
    }
    suspend fun writeAnkete(context: Context,anketa:Anketa) : String?{ //upisuje anketu u bazu
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase1.getInstance(context)
                db!!.anketaDao().insertAll(anketa)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
}
