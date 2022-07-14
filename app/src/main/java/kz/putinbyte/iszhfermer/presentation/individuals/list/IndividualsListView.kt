package kz.putinbyte.iszhfermer.presentation.individuals.list

import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.entities.iszh.Reference
import kz.putinbyte.iszhfermer.presentation.base.BaseView

interface IndividualsListView : BaseView {

    @AddToEndSingle
    fun showContTypes(items: List<Reference>)
}