package kz.putinbyte.iszhfermer.entities.db.animals.sickness

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_sickness_item")
data class SicknessCauseItem(
    val nameKz: String, // Убой
    val nameRu: String, // Убой
    val text: Int, // 1
    @PrimaryKey(autoGenerate = false)
    val value: Int // 1
)
