package ba.etf.rma22.projekat.viewmodel

import android.util.Log
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.AnketaTaken
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AnketaListViewModel {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

   fun getAnkete(onSuccess: (ankete: MutableList<Anketa>) -> Unit, onError: () -> Unit){
       scope.launch{
           val result = AnketaRepository.getAll()
           when (result) {
               is MutableList<Anketa> -> onSuccess?.invoke(result)
               else-> onError?.invoke()
           }
       }
   }
    fun getMyAnkete(onSuccess: (ankete: MutableList<Anketa>) -> Unit, onError: () -> Unit){
        scope.launch{
            var result = AnketaRepository.getUpisane()
            if(result==null)result= mutableListOf()
            when (result) {
                is MutableList<Anketa> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun getDone(onSuccess: (ankete: MutableList<Anketa>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = AnketaRepository.getDone()
            when (result) {
                is MutableList<Anketa> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun getNotTaken(onSuccess: (ankete: MutableList<Anketa>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = AnketaRepository.getNotTaken()
            when (result) {
                is MutableList<Anketa> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun getFuture(onSuccess: (ankete: MutableList<Anketa>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = AnketaRepository.getFuture()
            when (result) {
                is MutableList<Anketa> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun zapocniAnketu(idAnkete:Int){
        scope.launch{
            TakeAnketaRepository.zapocniAnketu(idAnkete)
            Log.d("2", "2 ")
        }
    }
    fun getPoceteAnkete():List<AnketaTaken>?{
        var result:List<AnketaTaken>?=null
        scope.launch{
            result = TakeAnketaRepository.getPoceteAnkete()
        }
        return result
    }
    fun getPocetuAnketu(idAnketa:Int,onSuccess: (t:Boolean) -> Unit, onError: () -> Unit){
        var result:List<AnketaTaken>?
        scope.launch{
            result = TakeAnketaRepository.getPoceteAnkete()
            if(result?.find { a->a.AnketumId==idAnketa }!=null){
                onSuccess?.invoke(true)
            }else onSuccess?.invoke(false)
        }
    }
    fun getOdgovori(idAnkete:Int, onSuccess: (odgovori: List<Odgovor>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = OdgovorRepository.getOdgovoriAnketa(idAnkete)
            when (result) {
                is List<Odgovor> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun postaviOdgovorAnketa(idAnketaTaken:Int,idPitanje:Int,odgovor:Int,
                             onSuccess: (odgovor: Int) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = OdgovorRepository.postaviOdgovorAnketa(idAnketaTaken,idPitanje,odgovor)
            when (result) {
                is Int -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun getAnketaById(id:Int, onSuccess: (anketa: Anketa) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = AnketaRepository.getById(id)
            when (result) {
                is Anketa -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun getAnketeByOffset(query: Int=-1, onSuccess: (ankete: List<Anketa>) -> Unit, onError: () -> Unit){
        scope.launch{
            var result:List<Anketa>?
            if(query!=-1)
                result = AnketaRepository.getAll(query)
            else
                result=AnketaRepository.getAll()
            when (result) {
                is List<Anketa> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }

}