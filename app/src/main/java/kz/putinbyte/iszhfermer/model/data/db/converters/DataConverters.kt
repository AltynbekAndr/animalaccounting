package  kz.putinbyte.iszhfermer.model.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.putinbyte.iszhfermer.entities.animals.AnimalBook
import kz.putinbyte.iszhfermer.entities.db.animals.Hoofed

class DataConverters {

    private val gson = Gson()

    private inline fun <reified T> Gson.fromJson(json: String?) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)


    private inline fun <reified T> Any.toJson(): String =
        gson.toJson(this, object : TypeToken<T>() {}.type)


    @TypeConverter
    fun fromStand(value: Hoofed?): String? = value?.toJson<Hoofed>()
    @TypeConverter
    fun toStand(value: String?): Hoofed? =  gson.fromJson(value)


    @TypeConverter
    fun fromAnimalBook(value: AnimalBook?): String? = value?.toJson<AnimalBook>()
    @TypeConverter
    fun toAnimalBook(value: String?): AnimalBook? =  gson.fromJson(value)



}