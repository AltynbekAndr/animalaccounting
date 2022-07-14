package kz.putinbyte.iszhfermer.entities.db.animals.sickness

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_sickness")
data class AnimalSickness(
    @PrimaryKey(autoGenerate = true)
    val animalId: Int = 0,
    val checked: Boolean = false,
    val comment: String? = null,
    var doctorId: Int = 0,
    val id: Int? = null,
    var initialDiagnosisId: Int = 0,
    val inj: String? = null,
    var sicknessCause: Int = 0,
    var sicknessRegDate: String = "",
    val success: Boolean = false
)
