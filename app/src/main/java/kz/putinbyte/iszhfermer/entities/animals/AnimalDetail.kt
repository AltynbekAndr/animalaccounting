package kz.putinbyte.iszhfermer.entities.animals

data class AnimalDetail(
    var id: Int = 0,
    var animalKindOfAnimal: String? = null,
    var mastItem: String? = null,
    var direction: String? = null,
    var import: String = "",
    var birthDate: String = "",
    var castrated: String? = null,
    var gender: String? = null,
    var motherInj: String? = null,
    var radioTag: String? = null,
    var owner: String? = null,
    var identity: String? = null,
    var inj: String = "",
    var importCountry: String? = null,
    var isBreed: String = "",
    var kato: String? = null,
    var comment: String? = null,
    var registrationDate:String? = null
)