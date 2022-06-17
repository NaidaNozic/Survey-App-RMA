package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.PitanjeAnketa

@Dao
interface PitanjeAnketaDAO {
    @Query("SELECT * FROM PitanjeAnketa")
    suspend fun getAllPitanjeAnketa(): List<PitanjeAnketa>

    @Insert
    suspend fun insertAll(vararg pitanjeAnketa: PitanjeAnketa)

    @Query("SELECT * FROM PitanjeAnketa WHERE AnketumId=:IdAnkete")
    suspend fun getAllPitanjeAnketaByIdAnkete(IdAnkete: Int): List<PitanjeAnketa>

    @Query("DELETE FROM PitanjeAnketa")
    suspend fun deleteAll():Int
}