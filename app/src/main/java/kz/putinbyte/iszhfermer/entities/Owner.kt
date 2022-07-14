package kz.putinbyte.iszhfermer.entities

class Owner(
    val id: String?,
    var ownerType: String?,
    var fullName: String?,
    var ogrn: String?,
    var tin: String?,
    var fio: String?,
    var individualRegistrationAddress: String?,
    var legalAddress: String?,
    var actualAddress: String?,
    var legalRegistrationAddress: String?,
    val documentType: String?,
    var documentSeries: String?,
    var documentNumber: String?,
    var documentIssueDate: String?,
    var email: String?,
    var phone: String?
//    val iszhIds: List<String>?,
)