package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.AnketaTaken

@Dao
interface AnketaTakenDAO {
    @Query("SELECT * FROM AnketaTaken")
    suspend fun getAll(): List<AnketaTaken>
    @Insert
    suspend fun insertAll(vararg anketaTaken: AnketaTaken)
    @Query("SELECT * FROM AnketaTaken WHERE AnketumId=:idAnkete")
    suspend fun getAnketaTakenByIdAnkete(idAnkete:Int):AnketaTaken?
    @Query("DELETE FROM AnketaTaken")
    suspend fun deleteAll():Int
    @Query("UPDATE AnketaTaken SET progres=:p WHERE id=:idAnketeTaken")
    suspend fun updateProgres(p:Int,idAnketeTaken:Int):Int
}