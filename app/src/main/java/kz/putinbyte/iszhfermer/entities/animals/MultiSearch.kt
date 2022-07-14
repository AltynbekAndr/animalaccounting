package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MultiSearch(
    @SerializedName("katoId")
    @Expose
    var katoId: Int? = null,

    @SerializedName("inj")
    @Expose
    var inj: Any? = null,

    @SerializedName("identifier")
    @Expose
    var identifier: Any? = null,

    @SerializedName("animalKindCode")
    @Expose
    var animalKindCode: String? = null,
)