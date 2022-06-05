package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.SveAnkete
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
        val result: List<Anketa> =a
        return result.sortedBy{it.datumPocetak}
    }
        suspend fun getNotTaken(): List<Anketa> {
            var rjesenje: MutableList<Anketa> = mutableListOf()
            for (s in getAll()!!) {
                if ((s.datumKraj != null && s.datumKraj!! < Date()) && s.progres < 100)
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

                var odgovori: List<Odgovor>
                if (response != null)
                    for (r in response) {
                        var poceteAnkete = TakeAnketaRepository.getPoceteAnkete()!!
                        var pocetaAnketa = poceteAnkete.find { p -> p.AnketumId == r.id }

                        if (pocetaAnketa != null)
                            odgovori =
                                pocetaAnketa?.let { OdgovorRepository.getOdgovoriAnketa(it.id) }!!
                        else odgovori = listOf()

                        var brPitanja = PitanjeAnketaRepository.getPitanja(r.id)?.size ?: 0

                        if (brPitanja == odgovori.size && brPitanja > 0 && (r.datumKraj == null || r.datumKraj!! > Date()) && r.datumPocetak < Date()) {
                            result.add(r)
                        }
                    }
                if(result==null)return@withContext null
                return@withContext result
            }
        }

        suspend fun getAll(offset: Int): List<Anketa>? {
            //vraća listu svih anketa ili ako je zadan offset
            //odgovarajući page u rezultatima (npr ako je pozvana metoda bez parametra vraćaju se
            //sve ankete, a ako je offset 1 vraća se samo prvih 5)
            var allAnkete: MutableList<Anketa> = mutableListOf()
            return withContext(Dispatchers.IO) {
                var response = ApiAdapter.retrofit.getAnkete(offset)
                val responseBody = response.body()
                if (responseBody != null)
                    for (a in responseBody) {
                        var grupe = ApiAdapter.retrofit.getGrupeAnkete(a.id).body()
                        var istrazivanja=grupe?.map { g->IstrazivanjeIGrupaRepository.getIstrazivanje(g.idIstrazivanja)?.naziv}?.distinct()
                        if(istrazivanja!=null)
                        for(i in istrazivanja){
                            if(a.nazivIstrazivanja==null || a.nazivIstrazivanja==" ")
                                a.nazivIstrazivanja= i.toString()
                            else a.nazivIstrazivanja=a.nazivIstrazivanja+","+i.toString()
                        }
                        allAnkete.add(a)
                    }
                allAnkete=dodijeliProgres1(allAnkete)
                return@withContext allAnkete.sortedBy { it.datumPocetak }
            }
        }

        suspend fun getById(id: Int): Anketa? {//radi
            //vraća jednu anketu koja ima zadani id ili null ako anketa ne postoji
            return withContext(Dispatchers.IO) {
                var response = ApiAdapter.retrofit.getAnketaById(id)
                val responseBody = response.body()
                if (responseBody != null && responseBody.message == "Anketa not found.") return@withContext null
                return@withContext responseBody
            }
        }

        suspend fun getUpisane(): List<Anketa>? {
            return withContext(Dispatchers.IO) {
                var response: Response<List<Anketa>>
                var myAnkete: MutableList<Anketa>? = mutableListOf()
                for (g in IstrazivanjeIGrupaRepository.getUpisaneGrupe()!!) {
                    SveAnkete.upisaneGrupe.add(g.naziv)
                    response = ApiAdapter.retrofit.getAnketeGrupe(g.id)
                    response.body()?.map { a -> a.nazivGrupe = g.naziv }
                    //naci istrazivanje za ovu grupu
                    var i = IstrazivanjeIGrupaRepository.getIstrazivanje(g.idIstrazivanja)
                    if (i != null) {
                        SveAnkete.upisanaIstrazivanja.add(i.naziv)
                    }
                    //dodijeliti tim anketama naziv istrazivanja
                    if (i != null) response.body()?.map { a -> a.nazivIstrazivanja = i.naziv }
                    response.body()?.let { myAnkete?.addAll(it) }
                }
                if (myAnkete != null) {
                    val iterator = myAnkete.iterator()
                    while (iterator.hasNext()) {
                        val item = iterator.next()
                        if (myAnkete.count { a ->
                                a.naziv == item.naziv
                                        && a.nazivIstrazivanja == item.nazivIstrazivanja
                            } > 1) iterator.remove()
                    }
                }
                myAnkete=dodijelitiProgres(myAnkete)
                if (myAnkete != null) myAnkete = myAnkete.sortedBy { it.datumPocetak }.toMutableList()
                if (myAnkete != null) if(myAnkete.size==0) {
                    return@withContext null
                }
                return@withContext myAnkete
            }
        }
    suspend fun dodijeliProgres1(ankete:MutableList<Anketa>): MutableList<Anketa> {
        if (ankete != null) {
            for(a in ankete){
                var poceteAnkete = TakeAnketaRepository.getPoceteAnkete()
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
                var poceteAnkete = TakeAnketaRepository.getPoceteAnkete()
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
}
