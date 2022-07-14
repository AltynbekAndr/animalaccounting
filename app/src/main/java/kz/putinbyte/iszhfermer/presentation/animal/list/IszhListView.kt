package kz.putinbyte.iszhfermer.presentation.animal.list

import kz.putinbyte.iszhfermer.entities.animals.AnimalList
import kz.putinbyte.iszhfermer.entities.BaseFormat
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface IszhListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<AnimalList.Animals>?)

    @AddToEndSingle
    fun showAnimalKindType(list:List<BaseFormat>)

    @Skip
    fun showErrorMessage(message: String)

    @Skip
    fun showProgressBar(show:Boolean)

}