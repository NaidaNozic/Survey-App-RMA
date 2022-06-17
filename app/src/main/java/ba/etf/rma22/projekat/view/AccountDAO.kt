package ba.etf.rma22.projekat.view

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Account
import ba.etf.rma22.projekat.data.models.Anketa

@Dao
interface AccountDAO {
    @Query("SELECT * FROM Account")
    suspend fun getAll(): List<Account>?
    @Query("DELETE FROM Account")
    suspend fun deleteAll(): Int
    @Insert
    suspend fun insertAll(vararg account: Account)
}