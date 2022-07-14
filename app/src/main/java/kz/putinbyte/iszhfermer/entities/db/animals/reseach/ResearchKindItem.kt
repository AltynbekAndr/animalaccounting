package kz.putinbyte.iszhfermer.entities.db.animals.reseach

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "research_kind_item")
data class ResearchKindItem(
    val code: String? = null,
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val nameKz: String? = null,
    val nameRu: String? = null,
)
