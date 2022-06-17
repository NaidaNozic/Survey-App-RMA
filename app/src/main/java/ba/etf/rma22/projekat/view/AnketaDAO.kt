package ba.etf.rma22.projekat.view

import androidx.room.*
import ba.etf.rma22.projekat.data.models.Anketa


@Dao
interface AnketaDAO { //s metodama za dobavljanje svih anketa i za upis anketa u bazu
    @Query("SELECT * FROM Anketa")
    suspend fun getAll(): List<Anketa>
    @Insert
    suspend fun insertAll(vararg anketa: Anketa)
    @Query("SELECT * FROM Anketa WHERE id=:AnketumId")
    suspend fun getAnketaById(AnketumId:Int):Anketa
    @Query("SELECT * FROM Anketa WHERE id >= :offset")
    suspend fun getAnketeByOffset(offset:Int):List<Anketa>

    @Query("DELETE FROM Anketa")
    suspend fun deleteAll():Int
}