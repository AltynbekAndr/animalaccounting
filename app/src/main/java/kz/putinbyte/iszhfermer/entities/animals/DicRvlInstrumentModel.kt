package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DicRvlInstrumentModel(
    @SerializedName("unitId")
    @Expose
    var unitId: Int? = null,

    @SerializedName("unitName")
    @Expose
    var unitName: String? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("code")
    @Expose
    var code: String? = null,

    @SerializedName("nameRu")
    @Expose
    var nameRu: String? = null,

    @SerializedName("nameKz")
    @Expose
    var nameKz: String? = null,

    var textValue:String? = null

)