package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class AnimalsModel (
    @SerializedName("multiSearch")
    @Expose
    var multiSearch: MultiSearch? = null,

    @SerializedName("pageIndex")
    @Expose
    var pageIndex: Int? = null,

    @SerializedName("pageSize")
    @Expose
    var pageSize: Int? = null
)