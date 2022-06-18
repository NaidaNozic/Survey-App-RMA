package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Odgovor1

@Dao
interface Odgovor1DAO {
    @Query("SELECT * FROM Odgovor1")
    suspend fun getAll(): List<Odgovor1>
    @Insert
    suspend fun insertAll(vararg odgovor1: Odgovor1)
    @Query("SELECT * FROM Odgovor1 WHERE AnketaTakenId=:anketaTakenId")
    suspend fun findOdgovoreAnkete(anketaTakenId: Int): List<Odgovor1>
    @Query("DELETE FROM Odgovor1")
    suspend fun deleteAll():Int
}