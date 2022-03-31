package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.models.Anketa

class AnketaListViewModel {
    fun getAnkete():List<Anketa>{
        return AnketaRepository.getAnkete();
    }
}