package kz.putinbyte.iszhfermer.entities

import kotlin.reflect.KProperty1 as AnyProp

data class BaseFormat(
    var id: Int?,
    val code: String?,
    val nameRu: String?,
    var nameKz: String?
) {
    companion object {
        fun getValue(instance: Any, propertyName: String): String {
            val property = instance::class.members
                .first { it.name == propertyName } as AnyProp<Any, *>
            return property.get(instance)?.toString() ?: ""
        }

        @Suppress("UNCHECKED_CAST")
        fun toFormat(items: List<Any>?,
                     id : String = "id",
                     code: String ="code",
                     nameRu: String = "nameRu",
                     nameKz: String = "nameKz"
        ): List<BaseFormat>? {
            return items?.mapNotNull { item ->
                try {
                    BaseFormat(
                        id = getValue(item, id).toInt(),
                        code = getValue(item, code),
                        nameRu = getValue(item, nameRu),
                        nameKz = getValue(item, nameKz)
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun fromUserData(items: List<Any>?): List<BaseFormat>? {
            return items?.mapNotNull { item ->
                try {
                    BaseFormat(
                        id = getValue(item, "id").toInt(),
                        code = getValue(item, "identifier"),
                        nameRu = getValue(item, "lastName") + " " +
                                getValue(item, "firstName") + " " +
                                getValue(item, "middleName"),
                        null
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }

        val defaultData = listOf(BaseFormat(0, null, "Выберите значение", "Мәнді таңдаңыз"))
        val defaultParam = listOf(BaseFormat(null, null, "Выберите значение", "Мәнді таңдаңыз"))
    }
}
