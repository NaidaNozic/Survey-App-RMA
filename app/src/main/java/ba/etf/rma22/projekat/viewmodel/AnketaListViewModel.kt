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