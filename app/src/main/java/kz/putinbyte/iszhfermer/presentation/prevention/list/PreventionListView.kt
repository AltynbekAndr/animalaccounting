package kz.putinbyte.iszhfermer.presentation.prevention.list

import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.entities.animals.AnimalPreventiveActionModelItem
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface PreventionListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<AnimalPreventiveActionModelItem>?)

    @Skip
    fun showLoader(show:Boolean)
}