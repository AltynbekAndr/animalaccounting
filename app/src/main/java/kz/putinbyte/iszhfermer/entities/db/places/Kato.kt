package kz.putinbyte.iszhfermer.entities.db.places

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "katos")
data class Kato(

    @Expose
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("code")
    val code: Int,

    @Expose
    @SerializedName("last")
    val last: Boolean,

    @Expose
    @SerializedName("nameKz")
    val nameKz: String,

    @Expose
    @SerializedName("nameRu")
    val nameRu: String,
)