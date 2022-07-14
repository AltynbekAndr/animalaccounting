package kz.putinbyte.iszhfermer.entities.animals

data class AnimalListBody(
    val multiSearch: MultiSearchSearch?,
    val pageIndex: Int,
    val pageSize: Int
)