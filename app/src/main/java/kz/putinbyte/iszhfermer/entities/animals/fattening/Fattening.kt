package kz.putinbyte.iszhfermer.entities.animals.fattening

data class Fattening(
    var animalId: Int = 0,
    var animalWeight: Int = 0,
    val animalWeightWhenRemove: Int = 0,
    var countryId: Int? = 0,
    val endDate: String? = null,
    var fatteningAreaId: Int? = 0,
    val id: Int? = null,
    var isAboardRK: Boolean = false,
    var isFamilyFeeding: Boolean = false,
    var migrationKatoId: Int? = null,
    val removeCauseId: Boolean = false,
    var startDate: String = ""
)