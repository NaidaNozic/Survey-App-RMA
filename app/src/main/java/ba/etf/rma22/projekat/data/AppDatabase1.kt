package ba.etf.rma22.projekat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.view.*

/*Imat ce companion objekat koji će instancirati bazu te će se
na taj način održati singleton pattern*/

@Database(entities = arrayOf(Anketa::class, Grupa::class, AnketaTaken::class, Odgovor1::class, Pitanje::class,
    PitanjeAnketa::class, AnketaIGrupe::class, Istrazivanje::class, Account::class, Odgovor::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase1 :RoomDatabase(){
    abstract fun anketaDao(): AnketaDAO
    abstract fun grupaDao(): GrupaDAO
    abstract fun anketaTakenDao(): AnketaTakenDAO
    abstract fun odgovor1DAO(): Odgovor1DAO
    abstract fun pitanjeDao(): PitanjeDAO
    abstract fun pitanjeAnketaDao(): PitanjeAnketaDAO
    abstract fun anketaIGrupeDao(): AnketaIGrupeDAO
    abstract fun istrazivanjeDao(): IstrazivanjeDAO
    abstract fun accountDao(): AccountDAO
    abstract fun odgovorDao(): OdgovorDAO

    companion object {
        private var INSTANCE: AppDatabase1? = null

        fun getInstance(context: Context): AppDatabase1 {
            if (INSTANCE == null) {
                synchronized(AppDatabase1::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase1::class.java,
                "RMA22DB"
            ).build()
    }
}