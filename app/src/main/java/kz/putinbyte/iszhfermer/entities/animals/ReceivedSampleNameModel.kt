package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class ReceivedSampleNameModel (
    @SerializedName("isPathalogical")
    @Expose
    var isPathalogical: Boolean? = null,

    @SerializedName("isAbleDownloadActReport")
    @Expose
    var isAbleDownloadActReport: Boolean? = null,

    @SerializedName("isAbleDownloadTubesReport")
    @Expose
    var isAbleDownloadTubesReport: Boolean? = null,

    @SerializedName("isAbleDownloadAccompanyingReport")
    @Expose
    var isAbleDownloadAccompanyingReport: Boolean? = null,

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