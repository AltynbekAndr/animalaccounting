package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class DicRvlBranchModel (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("code")
    @Expose
    var code: Any? = null,

    @SerializedName("nameRu")
    @Expose
    var nameRu: String? = null,

    @SerializedName("nameKz")
    @Expose
    var nameKz: String? = null,

    @SerializedName("literCode")
    @Expose
    var literCode: String? = null,

    @SerializedName("codeKato")
    @Expose
    var codeKato: Any? = null
)