package kz.putinbyte.iszhfermer.entities.db.places

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "countries")
data class Country(

    @Expose
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("code")
    val code: String?,

    @Expose
    @SerializedName("nameKz")
    val nameKz: String?,

    @Expose
    @SerializedName("nameRu")
    val nameRu: String?,
)
