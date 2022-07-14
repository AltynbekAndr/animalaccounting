package kz.putinbyte.iszhfermer.entities.animals

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.utils.LogUtils
import org.w3c.dom.Comment
import java.io.Serializable
import java.lang.Exception
import kotlin.reflect.KProperty1

data class AnimalList(
    val count: Int, // 41
    val listAnimals: List<Animals>
) : Serializable {
    data class Animals(
        val age: String? = null, // 0 лет 1 м.
        var animalKind: String? = null, // null
        val animalKindId: Int? = null, // 1
        val birthDate: String? = null, // 2021-11-10T16:56:00.006
        val bloodRelationship: Any? = null, // null
        val country: String? = null, // null
        val countryId: Int? = null,
        val fattening: Boolean, // false
        var genderId: Int? = null, // 1
        val id: Int, // 7567559
        val importCountryId: Int? = null, // 64
        val inj: String? = null, // KZL102321660
        var mast: String? = null, // null
        val mastId: Int? = null, // 2691
        var pledged: Boolean, // false
        var gender: String? = null,
        var comment: String? =null
    ) {
        companion object {
            fun toFormat(items: List<RegAnimal>): List<Animals> {
                return items.map { item ->
                    Animals(
                        id = item.id!!,
                        animalKindId = item.animalKindId,
                        birthDate = item.birthDate,
                        genderId = item.genderId,
                        importCountryId = item.importCountryId,
                        inj = item.inj,
                        mastId = item.mastId,
                        countryId = item.countryId,
                        fattening = false,
                        pledged = false,
                        comment = item.comment
                    )

                }
            }
        }
    }
}