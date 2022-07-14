package kz.putinbyte.iszhfermer.entities.iszh

import com.google.gson.Gson

class Сharacteristics(
    val id: String?,
    val iszhId: String?,
    var iszhUnderlyingSurfaceType: String?,
    var iszhFencesType: String?,
    var fencedSidesNumber: String?,
    var canopy: Boolean?,
    var type: String?,
    var separateWasteCollection: Boolean?,
    var square: String?,
    var warehouseType: String?
) {

    fun clone(): Сharacteristics {
        val stringCharacteristics = Gson().toJson(this, Сharacteristics::class.java)
        return Gson().fromJson<Сharacteristics>(
            stringCharacteristics,
            Сharacteristics::class.java
        )
    }
}