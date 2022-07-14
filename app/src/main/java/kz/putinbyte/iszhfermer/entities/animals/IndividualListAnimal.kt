package kz.putinbyte.iszhfermer.entities.animals

data class IndividualListAnimal(
    val column: String,
    val direction: String,
    val multiSearch: MultiSearch,
    val pageIndex: Int = 0,
    val pageSize: Int = 40,
    val search: String
) {
    data class MultiSearch(
        val additionalProp1: String,
        val additionalProp2: String,
        val additionalProp3: String
    )
}