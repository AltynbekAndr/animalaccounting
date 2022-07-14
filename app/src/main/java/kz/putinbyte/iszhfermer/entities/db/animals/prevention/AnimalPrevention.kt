package kz.putinbyte.iszhfermer.entities.db.animals.prevention

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_prevention")
data class AnimalPrevention(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val checked: Boolean = false,
    val animalId: Int = 0,
    var immunKindId: Int  = 0,
    var immunizationDate: String? = null,
    var sicknessId: Int = 0,
    var doctorId: Int = 0,
    val inj: String? = null,
    val success: Boolean = false,
    val comment: String? = null
)
