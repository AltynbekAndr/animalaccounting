package kz.putinbyte.iszhfermer.entities.animals

import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.db.places.Kato

data class ScanData(
    var inj: String? = null,
    var causeRegistration: Registration? = null,
    var typeIdentification: Identity? = null,
    var animalKind: KindOfAnimal? = null,
    var kato: Kato? = null,
    var country: Country? = null
)
