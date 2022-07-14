package kz.putinbyte.iszhfermer.entities.animals.success

data class SuccessList(
    val actNumber: String?,
    val dateCreate: String?,
    val examinationActDate: String?,
    val laborantName: String?,
    val organizationNameKz: String?,
    val organizationNameRu: String?,
    val receiverName: String?,
    val researchKind: ResearchKind?,
    val resultId: Int?,
    val resultNameRu: String?,
    val sickness: Sickness?,
    val tubeNumber: String?
)