package kz.putinbyte.iszhfermer.entities.sickness

import com.google.gson.annotations.SerializedName
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import kz.putinbyte.iszhfermer.entities.network.GroupAnimalPrevention
import kz.putinbyte.iszhfermer.entities.research.GroupAnimalResearch

data class GroupPreventiveActions(
    @SerializedName("item1")
    val item1: GroupAnimalSickness,
    val item2: List<Item2>?
) {
    data class Item2(
        val animalId: Int, // 0
        val comment: String, // Ошибка при групповой вставке
        val id: Int, // 2
        val success: Boolean // false
    )
}

data class GroupResearchActions(
    @SerializedName("item1")
    val item1: GroupAnimalResearch,
    val item2: List<Item2>?
) {
    data class Item2(
        val animalId: Int, // 0
        val comment: String, // Ошибка при групповой вставке
        val id: Int, // 2
        val success: Boolean // false
    )
}

data class GroupPreventionActions(
    @SerializedName("item1")
    val item1: GroupAnimalPrevention,
    val item2: List<Item2>?
) {
    data class Item2(
        val animalId: Int, // 0
        val comment: String, // Ошибка при групповой вставке
        val id: Int, // 2
        val success: Boolean // false
    )
}