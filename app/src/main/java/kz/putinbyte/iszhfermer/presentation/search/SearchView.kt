package kz.putinbyte.iszhfermer.presentation.search

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.search.SearchResponse
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface SearchView : BaseView {

    @AddToEndSingle
    fun setList(list: ArrayList<SearchResponse.Lists>, search: Boolean = false)

    @AddToEndSingle
    fun showKatoName(katoName:String?)

    @AddToEndSingle
    fun showKindOfAnimal(items: List<BaseFormat>)

    @AddToEndSingle
    fun showStatus(items: List<BaseFormat>)

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun showEmptyMessage()
}