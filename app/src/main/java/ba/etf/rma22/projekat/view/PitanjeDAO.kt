package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Pitanje

@Dao
interface PitanjeDAO {
    @Query("SELECT * FROM Pitanje")
    suspend fun getAll(): List<Pitanje>
    @Insert
    suspend fun insertAll(vararg pitanje: Pitanje)
    @Query("SELECT * FROM Pitanje WHERE id=:pitanjeId")
    suspend fun getPitanjaById(pitanjeId:Int): Pitanje
    @Query("DELETE FROM Pitanje")
    suspend fun deleteAll():Int

}