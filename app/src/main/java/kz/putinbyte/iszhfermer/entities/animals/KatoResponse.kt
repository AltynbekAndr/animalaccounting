package kz.putinbyte.iszhfermer.entities.animals

data class KatoResponse(
    val animalAmountByKatos: List<AnimalAmountByKato>,
    val katos: List<Kato>
) {
    data class AnimalAmountByKato(
        val camelsCount: Int,
        val cattleCount: Int,
        val code: Int,
        val hoofedsCount: Int,
        val horsesCount: Int,
        val id: Int,
        val isUserKato: Boolean,
        val last: Boolean,
        val name: String,
        val pigsCount: Int,
        val smallCattlesCount: Int
    )
    data class Kato(
        val code: Int,
        val id: Int,
        val isUserKato: Boolean,
        val last: Boolean,
        val nameKz: String,
        val nameRu: String
    )
}
