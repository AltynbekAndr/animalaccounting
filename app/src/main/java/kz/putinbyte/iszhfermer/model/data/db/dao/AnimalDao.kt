package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.*
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal

@Dao
interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animals: List<RegAnimal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(animal: RegAnimal)

    @Transaction
    @Query("select * from reg_animal")
    suspend fun getAll(): List<RegAnimal>?

    @Delete
    fun delete(regAnimal: RegAnimal)

    @Transaction
    @Query("select * from reg_animal where id=:id limit 1")
    suspend fun getById(id: Int): RegAnimal?

}