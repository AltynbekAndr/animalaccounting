package  kz.putinbyte.iszhfermer.model.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonElement

import com.google.gson.JsonParser
import kz.putinbyte.iszhfermer.entities.db.animals.Hoofed
import kz.putinbyte.iszhfermer.entities.db.animals.SmallCattle
import java.lang.Exception


class ListDataConverters {

    private val gson = Gson()

    private inline fun <reified T> fromJson(json: String?, cls: Class<T>?): List<T> {
        val list: MutableList<T> = ArrayList()
        try {
            val gson = Gson()
            val arry = JsonParser().parse(json).asJsonArray
            for (jsonElement in arry) {
                list.add(gson.fromJson(jsonElement, cls))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }


    private inline fun <reified T> Any.toJson(): String =
        gson.toJson(this, object : TypeToken<List<T>>() {}.type)


    @TypeConverter
    fun fromHoofed(value: List<Hoofed>?): String? = value?.toJson<Hoofed>()
    @TypeConverter
    fun toHoofed(value: String?): List<Hoofed>? = if (value != null) fromJson(value,Hoofed::class.java) else null


    @TypeConverter
    fun fromSmallCattle(value: List<SmallCattle>?): String? = value?.toJson<SmallCattle>()
    @TypeConverter
    fun toSmallCattle(value: String?): List<SmallCattle>? =  if (value != null) fromJson(value,SmallCattle::class.java) else null


}