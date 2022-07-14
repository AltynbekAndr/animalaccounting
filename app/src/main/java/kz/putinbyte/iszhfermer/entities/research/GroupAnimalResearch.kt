package kz.putinbyte.iszhfermer.entities.research

import com.google.gson.annotations.SerializedName
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch


data class GroupAnimalResearch(
    @SerializedName("groupAnimalResearchDtos")
    val animalResearchDtos: List<AnimalResearch>
)

