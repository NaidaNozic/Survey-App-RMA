package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa

@Dao
interface GrupaDAO {
    @Query("SELECT * FROM Grupa")
    suspend fun getAllGrupe(): List<Grupa>
    @Insert
    suspend fun insertAll(vararg grupa: Grupa)
    @Query("SELECT * FROM Grupa WHERE id=:idGrupe")
    suspend fun getGrupaById(idGrupe:Int): Grupa
    @Query("SELECT * FROM Grupa WHERE upisana=1")
    suspend fun getUpisaneGrupe(): List<Grupa>
    @Query("UPDATE Grupa SET upisana=1 WHERE id=:idGrupe")
    suspend fun postaviGrupuKaoUpisanu(idGrupe:Int):Int
    @Query("DELETE FROM Grupa")
    suspend fun deleteAll():Int
}