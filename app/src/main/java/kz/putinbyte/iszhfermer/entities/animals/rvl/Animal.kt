package kz.putinbyte.iszhfermer.entities.animals.rvl

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Animal (
    @SerializedName("animalId")
    @Expose
    var animalId: Int? = null,

    @SerializedName("tubeNumber")
    @Expose
    var tubeNumber: Int? = null,

    @SerializedName("specialMarks")
    @Expose
    var specialMarks: String? = null
)