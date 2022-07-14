package kz.putinbyte.iszhfermer.entities.animals.replaceinj

data class ReplaceInj(
    var animalId: Int = 0,
    var dateIssue: String = "",
    var identId: Int = 0,
    var import: Boolean = false,
    var inj: String = "",
    var note: String? = null,
)
