package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.entities.db.places.Kato

@Dao
interface KatoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(katos: List<Kato>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kato: Kato)

    @Transaction
    @Query("select * from katos")
    suspend fun getAll(): List<Kato>?

}