package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class IstrazivanjeIGrupaViewModel {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun getGrupe(onSuccess: (grupe: List<Grupa>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = IstrazivanjeIGrupaRepository.getGrupe()
            when (result) {
                is List<Grupa> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun getIstrazivanja(onSuccess: (istrazivanja: List<Istrazivanje>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = IstrazivanjeIGrupaRepository.getIstrazivanja()
            when (result) {
                is List<Istrazivanje> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
    fun upisiUGrupu(idGrupe: Int):Boolean{
        var upis= false
        scope.launch {
            upis=IstrazivanjeIGrupaRepository.upisiUGrupu(idGrupe)
        }
        return upis
    }
    fun getUpisaneGrupe(onSuccess: (grupe: List<Grupa>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result=IstrazivanjeIGrupaRepository.getUpisaneGrupe()
            when (result) {
                is List<Grupa> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
}