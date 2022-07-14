package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.ResearchKindItem
import kz.putinbyte.iszhfermer.entities.network.ResearchResultItem
@Dao
interface PreventionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPrevention(researches: List<AnimalPrevention>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrevention(sicknesses: AnimalPrevention)

    @Transaction
    @Query("select * from animal_prevention")
    suspend fun getAllPrevention(): List<AnimalPrevention>?

    @Delete
    fun delete(animalResearch: AnimalPrevention)
}