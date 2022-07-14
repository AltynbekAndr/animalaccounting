package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.ResearchKindItem
import kz.putinbyte.iszhfermer.entities.network.ResearchResultItem

@Dao
interface ResearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllResearchKind(researchKindItem: List<ResearchKindItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResearchKind(researchKindItem: ResearchKindItem)

    @Transaction
    @Query("select * from research_kind_item")
    suspend fun getAllResearchKind(): List<ResearchKindItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllResearchResult(researchResults: List<ResearchResultItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResearchResult(researchResults: ResearchResultItem)

    @Transaction
    @Query("select * from research_result")
    suspend fun getAllResearchResult(): List<ResearchResultItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllResearches(researches: List<AnimalResearch>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResearches(sicknesses: AnimalResearch)

    @Transaction
    @Query("select * from animal_research")
    suspend fun getAllResearches(): List<AnimalResearch>?

    @Delete
    fun delete(animalResearch: AnimalResearch)

}