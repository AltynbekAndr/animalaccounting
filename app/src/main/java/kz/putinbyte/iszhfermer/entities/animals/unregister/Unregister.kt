package kz.putinbyte.iszhfermer.entities.animals.unregister

data class Unregister(
    val animalId: Int = 0,
    var cause: Int = 0,
    val areaId: Int? = null,
    var causeComment: String = "",
    var comment: String? = null,
    var endDate: String = "",
    var newKatoId: Int = 0,
    var newOwnerId: Int = 0,
    var sicknessId: Int = 0,
    val statusCode: Int? = null
)