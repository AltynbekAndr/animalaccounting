package kz.putinbyte.iszhfermer.entities.animals.search

data class QuarantineDto(
    val comment: Any,
    val fileId: Any,
    val quarantineEndDate: String,
    val quarantineStartDate: String
)