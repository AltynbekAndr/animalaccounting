package kz.putinbyte.iszhfermer.entities.animals.owners

data class OwnersResponse(
    val page: Int,
    val pageSize: Int,
    val paged: Boolean,
    val softCreatedDate: String
)