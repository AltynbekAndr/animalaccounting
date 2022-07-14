package kz.putinbyte.iszhfermer.presentation.ownersList

import kz.putinbyte.iszhfermer.entities.animals.OwnersByKato
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface OwnersListView : BaseView {

    @AddToEndSingle
    fun setList(list:List<OwnersByKato>)

    @Skip
    fun showProgressBar(show:Boolean)
}