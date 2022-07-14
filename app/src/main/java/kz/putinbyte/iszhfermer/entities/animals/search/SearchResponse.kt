package kz.putinbyte.iszhfermer.entities.animals.search

data class SearchResponse(
    val count: Int,
    val lists: List<Lists>
){
    data class Lists(
        val age: String,
        val ageGroupCode: Int,
        val ageGroupId: Int,
        val animalKindId: Int,
        var animalKindString:String?,
        val birthDate: String,
        val causeId: Int,
        var genderId: Int,
        var genderString: String?,
        val greenCorridor: Int,
        val id: Int,
        val import: Boolean,
        val inQuarantine: Boolean,
        val inj: String,
        val isGreenCorridor: Boolean,
        val katoBreadCrumbs: String,
        val katoId: Int,
        val mastId: Int,
        var mastString: String?,
        val owner: String,
        var ownerFullName: String,
        val ownerId: Int,
        val quarantineDtos: List<QuarantineDto>,
        val registrationDate: String,
        val spk: Any,
        val spks: Boolean,
        val status: Int,
        val te: Int,
        val unRegisterDate: String
    )
}
