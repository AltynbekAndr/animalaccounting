package kz.putinbyte.iszhfermer.entities.animals.rvl

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class SicknessWithTube (
    @SerializedName("infective")
    @Expose
    var infective: Boolean? = null,

    @SerializedName("parentId")
    @Expose
    var parentId: Any? = null,

    @SerializedName("isAnyRvlReason")
    @Expose
    var isAnyRvlReason: Boolean? = null,

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

    @SerializedName("selectedTubes")
    @Expose
    var selectedTubes: List<SelectedTube>? = null,

    @SerializedName("rvlReason")
    @Expose
    var rvlReason: Int? = null,

    @SerializedName("repeated")
    @Expose
    var repeated: Boolean? = null
)