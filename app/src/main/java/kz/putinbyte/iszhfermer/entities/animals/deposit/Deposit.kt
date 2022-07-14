package kz.putinbyte.iszhfermer.entities.animals.deposit

data class Deposit(
    val id: Int? = null,
    var animalId: Int = 0,
    var pledgerId: Int = 0,
    var borrowerId: Int = 0,
    var pledgeeId: Int = 0,
    var registerNumber: String = "",
    var contractNumber: String = "",
    var contractEndDate: String = "",
    var contractStartDate: String = "",
    var fileId: Int = 0,
    var note: String? = null,
    var pledgeSum: Double = 0.0
)
