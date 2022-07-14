package kz.putinbyte.iszhfermer.entities.network

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "research_result")
data class ResearchResultItem(
    @PrimaryKey(autoGenerate = false)
    val text: Int = 0,
    val nameKz: String = "",
    val nameRu: String = "",
    val value: Int = 0
)
