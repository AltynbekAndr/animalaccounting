package kz.putinbyte.iszhfermer.entities.requests

data class PropertyType(
    val code: Int? = null, // null
    val id: Int, // 1
    val nameKz: String, // Жеке кәсіпкер
    val nameRu: String, // Индивидуальный предприниматель HH
    val shortNameKz: String, // ЖК
    val shortNameRu: String // ИП
)
