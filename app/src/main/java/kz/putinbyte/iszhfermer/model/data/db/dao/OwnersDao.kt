package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners

@Dao
interface OwnersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(owners: List<Owners>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(owner: Owners)

    @Transaction
    @Query("select * from owners")
    suspend fun getAll(): List<Owners>?
}