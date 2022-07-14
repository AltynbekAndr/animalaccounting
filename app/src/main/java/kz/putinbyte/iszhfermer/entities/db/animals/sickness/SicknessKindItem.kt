package kz.putinbyte.iszhfermer.entities.db.animals.sickness

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sickness_kind_item")
data class SicknessKindItem(
    val code: String? = null, // 613
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null, // 287
    val infective: Boolean? = null, // false
    val isAnyRvlReason: Boolean? = null, // false
    val nameKz: String? = null, // Алиментарная дистрофия
    val nameRu: String? = null, // Алиментарная дистрофия
    val parentId: Int? = null // 612
)
