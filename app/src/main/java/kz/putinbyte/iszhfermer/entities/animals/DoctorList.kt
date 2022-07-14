package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class DoctorList (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("identifier")
    @Expose
    var identifier: String? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("middleName")
    @Expose
    var middleName: String? = null,

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null
)