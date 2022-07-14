package kz.putinbyte.iszhfermer.entities.network.region

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("animalAmountByKatos")
    val animalAmountByKatos: List<AnimalAmountByKato>,
    @SerializedName("katos")
    val katos: List<Kato>
) {
    data class AnimalAmountByKato(
        val camelsCount: Int, // 0
        val cattleCount: Int, // 49317
        val code: Int, // 111010000
        val hoofedsCount: Int, // 0
        val horsesCount: Int, // 1
        val id: Int, // 3
        val isUserKato: Boolean, // true
        val last: Boolean, // true
        val name: String, // Г.КОКШЕТАУ
        val pigsCount: Int, // 0
        val smallCattlesCount: Int // 21
    )

    data class Kato(
        val code: Int, // 110000000
        val id: Int, // 1
        val isUserKato: Boolean, // false
        val last: Boolean, // false
        val nameKz: String, // АҚМОЛА ОБЛЫСЫ
        val nameRu: String // АКМОЛИНСКАЯ ОБЛАСТЬ
    )
}