package kz.putinbyte.iszhfermer.entities.db.animals

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kz.putinbyte.iszhfermer.entities.animals.AnimalBook
import kz.putinbyte.iszhfermer.model.data.db.converters.DataConverters

@Entity(tableName = "reg_animal")
data class RegAnimal(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var comment: String? = null,
    var isSuccess: Boolean = false,
    var animalKindId: Int = 0,
    var mastId: Int = 0,
    var directionId: Int = 0,
    var import: Boolean = false,
    var birthDate: String = "",
    var castrated: Boolean? = null,
    var genderId: Int = 0,
    var motherInj: String? = null,
    var radioTag: String? = null,
    var causeId: Int = 0,
    var ownerId: Int? = null,
    var fullName: String? = null,
    var identId: Int = 0,
    var inj: String = "",
    var katoId: Int = 0,
    var countryId: Int = 0,
    var registrationDate:String? = null,
    var importCountryId: Int = 0,
    var parentInfo: String? = null,
    var isBreed: Boolean = false,
    var breedKindId: Int? = null,
    var classId: Int? = null,
    var greenCorridor: Int? = null,
    @TypeConverters(DataConverters::class)
    var animalBook: AnimalBook? = null,
    var ownerINN: String? = null
)