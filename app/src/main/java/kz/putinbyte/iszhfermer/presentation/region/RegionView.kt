package kz.putinbyte.iszhfermer.presentation.region

import kz.putinbyte.iszhfermer.entities.animals.KatoResponse
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface RegionView : BaseView {

    @AddToEndSingle
    fun setList(list: List<KatoResponse.AnimalAmountByKato>)

}