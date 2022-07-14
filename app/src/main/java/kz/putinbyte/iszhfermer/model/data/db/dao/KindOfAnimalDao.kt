package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal

@Dao
interface KindOfAnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kindOfAnimals: List<KindOfAnimal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kindOfAnimal: KindOfAnimal)

    @Transaction
    @Query("select * from kind_of_animal")
    suspend fun getAll(): List<KindOfAnimal>?

}