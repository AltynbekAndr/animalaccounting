package kz.putinbyte.iszhfermer.entities.network

import kz.putinbyte.iszhfermer.entities.db.DoctorTypes

data class DoctorType(
    val count: Int, // 21
    val lists: List<DoctorTypes>
)