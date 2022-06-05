package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.repositories.PitanjeAnketaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PitanjaViewModel (){
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun getPitanjaByAnketa(query: Int, onSuccess: (pitanja: List<Pitanje>) -> Unit, onError: () -> Unit){
        scope.launch{
            val result = PitanjeAnketaRepository.getPitanja(query)
            when (result) {
                is MutableList<Pitanje> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
}