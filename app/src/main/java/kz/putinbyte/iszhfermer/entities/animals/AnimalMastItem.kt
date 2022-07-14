package kz.putinbyte.iszhfermer.entities.animals

import androidx.room.Entity
import androidx.room.PrimaryKey

data class AnimalMastItem(
    val animalKindId: Int, // 1
    val code: String?, // 1
    val directionId: Int, // 5
    val id: Int, // 2159
    val nameKz: String, // қара шұбар
    val nameRu: String // черно пестрая
)
