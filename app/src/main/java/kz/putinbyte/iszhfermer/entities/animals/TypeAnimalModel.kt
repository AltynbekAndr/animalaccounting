package kz.putinbyte.iszhfermer.entities.animals

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class TypeAnimalModel {
    @SerializedName("smallCattle")
    @Expose
    var smallCattleModel: List<SmallCattleModel>? = null

    @SerializedName("hoofed")
    @Expose
    var hoofed: List<HoofedModel>? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("nameRu")
    @Expose
    var nameRu: String? = null

    @SerializedName("nameKz")
    @Expose
    var nameKz: String? = null
}