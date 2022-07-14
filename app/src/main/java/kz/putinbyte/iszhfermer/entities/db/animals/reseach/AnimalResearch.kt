package kz.putinbyte.iszhfermer.entities.db.animals.reseach

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_research")
data class AnimalResearch(
    @PrimaryKey(autoGenerate = true)
    val animalId: Int = 0,
    val checked: Boolean = false,
    var comment: String? = null,
    var doctorId: Int = 0,
    val id: Int? = null,
    var researchKindId: Int = 0,
    var sicknessRegDate: String = "",
    var sicknessId: Int = 0,
    var sicknessCause: Int = 0,
    val inj: String? = null,
    val success: Boolean = false
)
