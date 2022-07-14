package kz.putinbyte.iszhfermer.entities.animals.replaceinj

data class ReplaceInjList(
    val animal: Int? = null,
    val animalId: Int?,
    val dateCreate: String?,
    var dateIssue: String?,
    val dateUpdate: String?,
    val id: Int?,
    val ident: Int? = null,
    var note: String? = null,
    val oldIdentId: Int?,
    val oldInj: String?,
    var user: String? = null,
    val userId: Int
)