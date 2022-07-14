package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import moxy.viewstate.strategy.StateStrategyType

class ListAnimalsModel (
    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("lists")
    @Expose
    var lists: List<ListModel>? = null
)