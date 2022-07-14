package kz.putinbyte.iszhfermer.presentation.research.list

import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface ResearchListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<AnimalResearchModelItem>)

    @Skip
    fun showLoader(show:Boolean)
}