package kz.putinbyte.iszhfermer.entities.animals.rvl

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Instrument (
    @SerializedName("instrumentId")
    @Expose
    var instrumentId: Int? = null,
    var value:Int? = null
)