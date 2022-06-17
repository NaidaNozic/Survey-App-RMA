package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Odgovor

@Dao
interface IstrazivanjeDAO {
    @Query("SELECT * FROM Istrazivanje")
    suspend fun getAllIstrazivanja(): List<Istrazivanje>
    @Insert
    suspend fun insertAll(vararg istrazivanje: Istrazivanje)

    @Query("DELETE FROM Istrazivanje")
    suspend fun deleteAll():Int
    @Query("SELECT * FROM Istrazivanje WHERE id=:IstrazivanjeId")
    suspend fun getIstrazivanjeById(IstrazivanjeId: Int): Istrazivanje
    @Query("SELECT * FROM Istrazivanje WHERE id>=:offset")
    suspend fun getIstrazivanjaByOffset(offset: Int): List<Istrazivanje>?

    @Query("SELECT * FROM Istrazivanje WHERE naziv=:name")
    suspend fun getIstrazivanjeByName(name: String): Istrazivanje
}