package kz.putinbyte.iszhfermer.entities.animals.success

data class Sickness(
    val childs: Any,
    val code: String,
    val dateCreate: String,
    val dateUpdate: String,
    val id: Int,
    val infective: Boolean,
    val isAnyRvlReason: Boolean,
    val isDangerous: Boolean,
    val isDeleted: Boolean,
    val monthPeriod: Int,
    val nameKz: String,
    val nameRu: String?,
    val parentId: Any,
    val sickness: Any,
    val sicknessCode: String
)