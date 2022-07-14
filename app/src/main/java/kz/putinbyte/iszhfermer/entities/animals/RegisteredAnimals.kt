package kz.putinbyte.iszhfermer.entities.animals

data class RegisteredAnimals(
    val animalAmountByKatos: List<AnimalAmountByKato>,
    val katos: List<Kato>
) {
    data class AnimalAmountByKato(
        val camelsCount: Int, // 0
        val cattleCount: Int, // 35
        val code: Int, // 110000000
        val hoofedsCount: Int, // 0
        val horsesCount: Int, // 1
        val id: Int, // 1
        val isUserKato: Boolean, // false
        val last: Boolean, // false
        val name: String, // АҚМОЛА ОБЛЫСЫ
        val pigsCount: Int, // 1
        val smallCattlesCount: Int // 1
    )

    data class Kato(
        val code: Int, // 0
        val id: Int, // 17015
        val isUserKato: Boolean, // false
        val last: Boolean, // false
        val nameKz: String, // ҚР
        val nameRu: String // РК
    )
}