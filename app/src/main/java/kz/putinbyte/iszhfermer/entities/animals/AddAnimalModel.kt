package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal

data class AddAnimalModel(
    @SerializedName("regAnimalDto")
    val regAnimal: List<RegAnimal>?
)