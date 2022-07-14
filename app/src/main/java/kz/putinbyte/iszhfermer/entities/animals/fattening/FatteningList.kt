package kz.putinbyte.iszhfermer.entities.animals.fattening

data class FatteningList(
    val animalId: Int,
    val animalWeight: Int?,
    val animalWeightWhenRemove: Int?,
    val code: String,
    val countryId: Int,
    val dateCreate: String?,
    val endDate: String?,
    val fatteningAreaId: Int,
    val id: Int,
    val isAboardRK: Boolean,
    val isFamilyFeeding: Boolean,
    val isFattening: Boolean,
    val migrationKatoId: Int,
    val nameRu: String?,
    val removeCauseId: String?,
    var inFattening:String?,
    val sicknessId: Int,
    val startDate: String?,
    val tubeNumber: Int?,
    val userId: Int
)