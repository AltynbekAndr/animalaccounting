package kz.putinbyte.iszhfermer.entities.network

class WasteSource(
    val id: String,
    val iszhId: String?,
    var sourceName: String?,
    var sourceType: String?,
    var cadastralNumber: String?,
    var fiasUUID: String?,
    val longitude: Double?,
    val latitude: Double?,
    var sourceCategory: String?,
    var subcategory: String?,
    var unitOfAccountType: String?,
    var alternativeUnitType: String?,
    var amountKg: String?,
    val amountM3: String?
)