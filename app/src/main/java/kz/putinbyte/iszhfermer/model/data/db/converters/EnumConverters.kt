package  kz.putinbyte.iszhfermer.model.data.db.converters

import androidx.room.TypeConverter
import kz.putinbyte.iszhfermer.component.NetworkAvailability
import kz.putinbyte.iszhfermer.entities.db.BaseFormatReferences

class EnumConverters {

    @TypeConverter
    fun toType(value: Int) = enumValues<BaseFormatReferences.ReferenceType>()[value]
    @TypeConverter
    fun fromType(value: BaseFormatReferences.ReferenceType) = value.ordinal

}