package kz.putinbyte.iszhfermer.entities.iszh

import kz.putinbyte.iszhfermer.entities.Owner
import kz.putinbyte.iszhfermer.entities.network.WasteSource
import javax.inject.Inject

class Iszh @Inject constructor(
    val id: String,
    var сharacteristics: Сharacteristics?,
    var iszhOperationalStatus: String?,
    val statusSettingDate: String?,
    var iszhStatus: String?,
    var longitude: Double?,
    var latitude: Double?,
    val iszhSourceType: String?,
    var owner: Owner?,
    var wasteSources: ArrayList<WasteSource>?,
    var isDraft: Boolean?,
    var responsibleFio: String?
) {

    fun removeSourceById(id: String) {
        val newSource = ArrayList<WasteSource>()
        for (item in wasteSources!!) {
            if (item.id != id) {
                newSource.add(item)
            }
        }
        wasteSources = newSource
    }

    fun setSource(source: WasteSource) {
        var isNew = true
        for ((index, item) in wasteSources!!.withIndex()) {
            if (source.id == item.id) {
                wasteSources!!.set(index, source)
                isNew = false
            }
        }
        if (isNew) wasteSources!!.add(source)
    }


}
