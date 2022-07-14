package kz.putinbyte.iszhfermer.presentation.issuedInj.list

import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInjList
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface IssuedInjListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<ReplaceInjList>?)

    @Skip
    fun showLoader(show:Boolean)
}