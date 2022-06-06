package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.models.JsonZaOdgovor
import ba.etf.rma22.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OdgovorRepository {

   suspend fun getOdgovoriAnketa(idAnkete:Int):List<Odgovor>?{
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
}