package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SmallCattleModel (
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
    var nameKz: String? = null
)