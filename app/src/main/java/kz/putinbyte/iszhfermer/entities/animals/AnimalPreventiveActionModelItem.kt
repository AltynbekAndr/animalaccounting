package kz.putinbyte.iszhfermer.entities.animals

data class AnimalPreventiveActionModelItem(
    val doctor: String? = null, // Алдабергенов   Балабай Шабанбайулы
    val doctorId: Int? = null, // 8
    val id: Int? = null, // 30
    val immunKindId: Int? = null, // 2
    var immunKind:String? = null,
    val immunizationDate: String?= null, // 2021-12-14T12:22:03.375
    val sickness: String? = null// Анаэробная дизентерия ягнят
)
