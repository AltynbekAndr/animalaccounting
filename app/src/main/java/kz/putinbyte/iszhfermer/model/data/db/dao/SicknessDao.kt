package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessCauseItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem

@Dao
interface SicknessDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSicknessCause(sicknessCauseItems: List<SicknessCauseItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSicknessCause(sicknessCauseItem: SicknessCauseItem)

    @Transaction
    @Query("select * from animal_sickness_item")
    suspend fun getAllSicknessCause(): List<SicknessCauseItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSicknessKindItem(sicknessKinds: List<SicknessKindItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSicknessKindItem(sicknessKinds: SicknessKindItem)

    @Transaction
    @Query("select * from sickness_kind_item")
    suspend fun getAllSicknessKindItem(): List<SicknessKindItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSicknesses(sicknesses: List<AnimalSickness>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSicknesses(sicknesses: AnimalSickness)

    @Transaction
    @Query("select * from animal_sickness")
    suspend fun getAllSicknesses(): List<AnimalSickness>?

    @Delete
    fun delete(animalSickness: AnimalSickness)

}