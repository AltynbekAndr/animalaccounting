package kz.putinbyte.iszhfermer.entities.animals.rvl

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class SelectedTube (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("isDead")
    @Expose
    var isDead: Boolean? = null,

    @SerializedName("inQuarantine")
    @Expose
    var inQuarantine: Boolean? = null,

    @SerializedName("identifier")
    @Expose
    var identifier: String? = null,

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null,

    @SerializedName("inj")
    @Expose
    var inj: String? = null,

    @SerializedName("mastName")
    @Expose
    var mastName: String? = null,

    @SerializedName("ageGroup")
    @Expose
    var ageGroup: AgeGroup? = null,

    @SerializedName("animalKind")
    @Expose
    var animalKind: AnimalKind? = null,

    @SerializedName("genderId")
    @Expose
    var genderId: Int? = null,

    @SerializedName("birthDate")
    @Expose
    var birthDate: String? = null,

    @SerializedName("isBlocked")
    @Expose
    var isBlocked: Boolean? = null,

    @SerializedName("blockedSicknessId")
    @Expose
    var blockedSicknessId: Int? = null,

    @SerializedName("isWellness")
    @Expose
    var isWellness: Boolean? = null,

    @SerializedName("blockedSicknessName")
    @Expose
    var blockedSicknessName: String? = null,

    @SerializedName("checked")
    @Expose
    var checked: Boolean? = null,

    @SerializedName("tubeNumber")
    @Expose
    var tubeNumber: Int? = null,

    @SerializedName("key")
    @Expose
    var key: Int? = null,

    @SerializedName("animalId")
    @Expose
    var animalId: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("disabled")
    @Expose
    var disabled: Boolean? = null,

    @SerializedName("direction")
    @Expose
    var direction: String? = null
)