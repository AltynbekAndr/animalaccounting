package kz.putinbyte.iszhfermer.presentation.addresses

import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface NameRegionsListView : BaseView {

    @AddToEndSingle
    fun setList(region: List<Region.AnimalAmountByKato>)

    @Skip
    fun showProgressBar(show:Boolean)
}