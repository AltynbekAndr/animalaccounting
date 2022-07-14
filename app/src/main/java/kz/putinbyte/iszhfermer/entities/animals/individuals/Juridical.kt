package kz.putinbyte.iszhfermer.entities.animals.individuals

data class Juridical(
    var bik: String? = null,
    var bin: String = "",
    var chiefFullName: String = "",
    var chiefPosition: String = "",
    var documentDateIssue: String = "",
    var documentNumber: String? = null,
    var doumentIssueBy: String = "",
    var email: String = "",
    var enterpriseTypeId: Int? = null,
    var flat: String? = null,
    var house: String? = null,
    var id: Int? = null,
    var katoId: Int = 0,
    var mobilePhone: String? = null,
    var nameKz: String? = null,
    var nameRu: String = "",
    var okedId: Int? = null,
    var okpoCode: String? = null,
    var opfId: Int = 0,
    var postIndex: String? = null,
    var shortNameRu: String = "",
    var street: String? = null,
    var tel: String? = null
)