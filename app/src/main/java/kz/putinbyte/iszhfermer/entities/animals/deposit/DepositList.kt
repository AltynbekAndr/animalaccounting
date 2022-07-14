package kz.putinbyte.iszhfermer.entities.animals.deposit

data class DepositList(
    val animalId: Int?,
    val borrowerId: Int?,
    val contractEndDate: String?,
    val contractNumber: String?,
    val contractStartDate: String?,
    val fileId: Int?,
    val id: Int?,
    val isPledged: Boolean,
    val note: String?,
    val pledgeSum: Double?,
    val pledgeeId: Int?,
    val pledgerId: Int?,
    val registerNumber: String?,
    val removePledgeFileId: Int?,
    val removePledgeNote: String?
)