package kz.putinbyte.iszhfermer.entities

import android.view.View
import kz.putinbyte.iszhfermer.model.data.enums.FieldType

data class FieldData(
    var fieldName: String,
    var fieldId: View,
    var fieldType: FieldType
)