package kz.putinbyte.iszhfermer.entities.network

import com.google.gson.annotations.SerializedName
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal

data class RegisterAnimals(
    @SerializedName("regAnimal")
    val regAnimalDto: List<RegAnimal>,
)
