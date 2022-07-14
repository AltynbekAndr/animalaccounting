package kz.putinbyte.iszhfermer.presentation.searchListView

import kz.putinbyte.iszhfermer.entities.animals.ListAnimalsModel
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface SearchListView : BaseView {
    @AddToEndSingle
    fun getList(list: ListAnimalsModel?)
}
