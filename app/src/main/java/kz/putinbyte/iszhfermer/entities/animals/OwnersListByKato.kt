package kz.putinbyte.iszhfermer.entities.animals

data class OwnersListByKato(
    val count: Int,
    val lists: List<OwnersByKato>
)