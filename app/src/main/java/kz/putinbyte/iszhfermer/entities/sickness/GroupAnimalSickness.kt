package kz.putinbyte.iszhfermer.entities.sickness

import com.google.gson.annotations.SerializedName
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness

data class GroupAnimalSickness(
    @SerializedName("groupAnimalSicknessDtos")
    val animalSickness: List<AnimalSickness>
)