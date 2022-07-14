package kz.putinbyte.iszhfermer.presentation.history.list

import kz.putinbyte.iszhfermer.entities.animals.history.HistoryList
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface HistoryListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<HistoryList>)

    @Skip
    fun showLoader(show:Boolean)
}