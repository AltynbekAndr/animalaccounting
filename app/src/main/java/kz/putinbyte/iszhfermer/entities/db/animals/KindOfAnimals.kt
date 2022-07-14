package kz.putinbyte.iszhfermer.entities.db.animals

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import  kz.putinbyte.iszhfermer.model.data.db.converters.ListDataConverters

@Entity(tableName = "kind_of_animal")
data class KindOfAnimal(

    @Expose
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("code")
    val code: String,

    @Expose
    @SerializedName("hoofed")
    @TypeConverters(ListDataConverters::class)
    val hoofed: List<Hoofed>?,

    @Expose
    @SerializedName("nameKz")
    val nameKz: String,

    @Expose
    @SerializedName("nameRu")
    val nameRu: String,

    @Expose
    @SerializedName("smallCattle")
    @TypeConverters(ListDataConverters::class)
    val smallCattle: List<SmallCattle>?
)

data class Hoofed(
    val id: Int,
    val code: String,
    val nameRu: String,
    val nameKz: String
)

data class SmallCattle(
    val id: Int,
    val code: String,
    val nameRu: String,
    val nameKz: String
)
