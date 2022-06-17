package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.AnketaIGrupe
import ba.etf.rma22.projekat.data.models.Grupa

@Dao
interface AnketaIGrupeDAO {
    @Query("SELECT * FROM AnketaIGrupe")
    suspend fun getAll(): List<AnketaIGrupe>
    @Query("SELECT * FROM AnketaIGrupe WHERE AnketumId=:idAnkete")
    suspend fun getAnketaGrupeByIdAnkete(idAnkete: Int): List<AnketaIGrupe>
    @Query("SELECT * FROM AnketaIGrupe WHERE GrupaId=:idGrupe")
    suspend fun getAnketaGrupeByIdGrupe(idGrupe: Int): List<AnketaIGrupe>
    @Insert
    suspend fun insertAll(vararg anketaIGrupe: AnketaIGrupe)
    @Query("DELETE FROM AnketaIGrupe")
    suspend fun deleteAll():Int
}