package kz.putinbyte.iszhfermer.entities.animals.history

data class HistoryList(
    val causeId: Int?,
    val comment: String?,
    val dateCreate: String?,
    val id: Int,
    val kato: String?,
    val katoId: Int?,
    val owner: String?,
    val ownerId: Int?,
    val sickness: String?,
    val sicknessId: Int?,
    val veterinarian: String?,
    val veterinarianId: Int?
)