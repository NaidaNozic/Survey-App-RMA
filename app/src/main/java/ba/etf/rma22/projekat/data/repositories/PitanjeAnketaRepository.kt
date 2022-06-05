package ba.etf.rma22.projekat.data.repositories

import ba.etf.rma22.projekat.data.ApiAdapter
import ba.etf.rma22.projekat.data.models.Pitanje
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PitanjeAnketaRepository {
    suspend fun getPitanja(idAnkete: Int): List<Pitanje>? {
        //vraća sva pitanja na anketi sa zadanim id-em
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.getPitanja(idAnkete)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }
}