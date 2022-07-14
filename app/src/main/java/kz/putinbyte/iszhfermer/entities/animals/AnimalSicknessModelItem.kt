package kz.putinbyte.iszhfermer.entities.animals


data class AnimalSicknessModelItem(
    val additionalResearch: Any?, // null
    val doctor: String?, // Абдукаримов Даурен
    val doctorId: Int?, // 5
    val finalDiagnosis: String?, // Африканская чума лошадей
    val id: Int?, // 793
    val initialDiagnosis: String?, // Асцит
    val note: String?, // null
    val sicknessCause: Int?, // 2
    var cause:String? = null,
    val sicknessRegDate: String? // 2021-12-22T11:46:27.161
)
