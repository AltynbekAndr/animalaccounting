package kz.putinbyte.iszhfermer.model.data.db.dao

import androidx.room.*
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.db.Identity

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<Country>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: Country)

    @Transaction
    @Query("select * from countries")
    suspend fun getAll(): List<Country>?
}