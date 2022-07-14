package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.DoctorTypes
import kz.putinbyte.iszhfermer.entities.db.Identity

@Dao
interface DoctorTypesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(identities: List<DoctorTypes>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(identity: DoctorTypes)

    @Transaction
    @Query("select * from doctor_types")
    suspend fun getAll(): List<DoctorTypes>?
}