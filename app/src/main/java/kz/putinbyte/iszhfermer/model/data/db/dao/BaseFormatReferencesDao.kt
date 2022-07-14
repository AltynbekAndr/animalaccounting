package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.BaseFormatReferences
import kz.putinbyte.iszhfermer.entities.db.places.Country

@Dao
interface BaseFormatReferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(references: List<BaseFormatReferences>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reference: BaseFormatReferences)

    @Transaction
    @Query("select * from base_format_references")
    suspend fun getAll(): List<BaseFormatReferences>?

    @Transaction
    @Query("select * from base_format_references where type =:referenceType")
    suspend fun getAllByType(referenceType: BaseFormatReferences.ReferenceType): List<BaseFormatReferences>?
}