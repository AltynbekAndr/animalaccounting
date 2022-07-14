package kz.putinbyte.iszhfermer.entities.animals

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class ListModel(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("isDead")
    @Expose
    var isDead: Boolean? = null,

    @SerializedName("identifier")
    @Expose
    var identifier: String? = null,

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null,

    @SerializedName("inj")
    @Expose
    var inj: String? = null,

    @SerializedName("ageGroup")
    @Expose
    var ageGroup: AgeGroupModel? = null,

    @SerializedName("animalKind")
    @Expose
    var animalKind: AnimalKindModel? = null,

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
):Parcelable{
    override fun toString(): String {
        return inj.toString()
    }

    override fun describeContents(): Int {
        return id!!
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {

    }
}