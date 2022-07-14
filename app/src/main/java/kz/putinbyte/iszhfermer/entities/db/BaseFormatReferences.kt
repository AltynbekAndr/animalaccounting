package kz.putinbyte.iszhfermer.entities.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.Gson
import kz.putinbyte.iszhfermer.model.data.db.converters.EnumConverters

@Entity(tableName = "base_format_references")
data class BaseFormatReferences (
    @PrimaryKey(autoGenerate = false)
    var id: Int? = 0,
    var code: String? = null,
    var nameRu: String?  = null,
    var nameKz: String? = null,
    @TypeConverters(EnumConverters::class)
    var type: ReferenceType = ReferenceType.IMMUN,
    ){
    enum class ReferenceType {
        IMMUN
    }
}