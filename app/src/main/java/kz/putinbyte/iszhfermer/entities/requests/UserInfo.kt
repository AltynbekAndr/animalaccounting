package kz.putinbyte.iszhfermer.entities.requests

data class UserInfoList(
    val count: Int? = null,
    val lists: List<UserInfo>
){
    data class UserInfo(
        val birthDate: String,
        val email: String,
        val firstName: String? = null,
        val login:String,
        val id: Int,
        val iin: String,
        val individualId: Int,
        val isBlock: Boolean,
        val isDeleted: Boolean,
        val isGreenCorridor: Boolean,
        val isPasswordExpired: Boolean,
        val isRvl: Boolean,
        val katoCode: Int,
        val katoId: Int,
        val lastName: String?,
        var fullName:String? = null,
        val middleName: String? = null,
        val orgStructure: OrgStructure,
        val orgStructureId: Int,
        val passwordDaysLeft: Int,
        val role: Role,
        val roleId: Int,
        val tel: String
    )
}
