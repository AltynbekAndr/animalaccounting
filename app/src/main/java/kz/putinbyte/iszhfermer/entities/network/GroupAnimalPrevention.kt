package kz.putinbyte.iszhfermer.entities.network

import com.google.gson.annotations.SerializedName
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention

data class GroupAnimalPrevention(
    @SerializedName("groupAnimalPreventiveActionsDto")
    val animalPrevention: List<AnimalPrevention>
)
