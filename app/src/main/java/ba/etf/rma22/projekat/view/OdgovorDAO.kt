package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Odgovor

@Dao
interface OdgovorDAO {
    @Query("SELECT * FROM Odgovor")
    suspend fun getAll(): List<Odgovor>
    @Insert
    suspend fun insertAll(vararg odgovor: Odgovor)
    @Query("DELETE FROM Odgovor")
    suspend fun deleteAll():Int
}