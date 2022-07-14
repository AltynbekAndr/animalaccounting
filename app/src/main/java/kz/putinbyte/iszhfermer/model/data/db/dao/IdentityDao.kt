package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.*

@Dao
interface IdentityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(identities: List<Identity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(identity: Identity)

    @Transaction
    @Query("select * from identities")
    suspend fun getAll(): List<Identity>?

}