package kz.putinbyte.iszhfermer.presentation.sicknesses.list

import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.entities.animals.AnimalSicknessModelItem
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface SicknessesListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<AnimalSicknessModelItem>?)

    @Skip
    fun showLoader(show:Boolean)
}