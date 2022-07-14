package kz.putinbyte.iszhfermer.entities.animals.owners

import kz.putinbyte.iszhfermer.entities.db.rvl.Owners

data class OwnersList(
    val count: Int,
    val list: List<Owners>?
)