package kz.putinbyte.iszhfermer.entities.animals.rvl

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class SaveRvlInventoryModel (
    @SerializedName("researchKindId")
    @Expose
    var researchKindId: Any? = null,

    @SerializedName("sicknessWithTubes")
    @Expose
    var sicknessWithTubes: List<SicknessWithTube>? = null,

    @SerializedName("receivedSampleId")
    @Expose
    var receivedSampleId: Int? = null,

    @SerializedName("documentNumber")
    @Expose
    var documentNumber: String? = null,

    @SerializedName("documentDate")
    @Expose
    var documentDate: String? = null,

    @SerializedName("rvlBranchId")
    @Expose
    var rvlBranchId: Int? = null,

    @SerializedName("pathalogicalMaterial")
    @Expose
    var pathalogicalMaterial: Any? = null,

    @SerializedName("dateOfIllness")
    @Expose
    var dateOfIllness: Any? = null,

    @SerializedName("dateOfDeath")
    @Expose
    var dateOfDeath: Any? = null,

    @SerializedName("clinicalPicture")
    @Expose
    var clinicalPicture: Any? = null,

    @SerializedName("autopsyData")
    @Expose
    var autopsyData: Any? = null,

    @SerializedName("presumptiveDiagnosis")
    @Expose
    var presumptiveDiagnosis: Any? = null,

    @SerializedName("excludeInfection")
    @Expose
    var excludeInfection: Any? = null,

    @SerializedName("dateOfSending")
    @Expose
    var dateOfSending: Any? = null,

    @SerializedName("isDdi")
    @Expose
    var isDdi: Any? = null,

    @SerializedName("repeated")
    @Expose
    var repeated: Boolean? = null,

    @SerializedName("witnessText")
    @Expose
    var witnessText: Any? = null,

    @SerializedName("sampleDateStart")
    @Expose
    var sampleDateStart: String? = null,

    @SerializedName("sampleDateEnd")
    @Expose
    var sampleDateEnd: String? = null,

    @SerializedName("doctors")
    @Expose
    var doctors: List<Int>? = null,

    @SerializedName("isPathalogical")
    @Expose
    var isPathalogical: Boolean? = null,

    @SerializedName("animals")
    @Expose
    var animals: List<Animal>? = null,

    @SerializedName("instruments")
    @Expose
    var instruments: List<Instrument>? = null
)