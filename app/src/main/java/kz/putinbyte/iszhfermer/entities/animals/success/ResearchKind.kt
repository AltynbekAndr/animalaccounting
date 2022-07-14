package kz.putinbyte.iszhfermer.entities.animals.success

data class ResearchKind(
    val code: String,
    val dateCreate: String,
    val dateUpdate: String,
    val id: Int,
    val isDeleted: Boolean,
    val nameKz: String,
    val nameRu: String?
)