package kz.putinbyte.iszhfermer.entities.animals.individuals

data class PhysicalResponse(
    val errors: Errors,
    val status: Int,
    val title: String,
    val traceId: String,
    val type: String
)