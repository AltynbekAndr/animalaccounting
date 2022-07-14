package kz.putinbyte.iszhfermer.entities.research

data class AnimalResearchModelItem(
    val cause: Int?, // 2
    var causeResult:String? = null,
    val doctor: String?, // Алдабергенов   Балабай Шабанбайулы
    val doctorId: Int?, // 8
    val id: Int, // 748
    val note: String?, // null
    val researchDate: String?, // 2021-12-07T12:08:08.647
    val researchKind: String?, // Серологическое
    val sickness: String? // Анаэробная дизентерия ягнят
)
