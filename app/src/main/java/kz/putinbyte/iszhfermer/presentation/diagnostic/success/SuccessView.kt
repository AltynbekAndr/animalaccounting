package kz.putinbyte.iszhfermer.presentation.diagnostic.success

import kz.putinbyte.iszhfermer.entities.animals.success.SuccessList
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import kz.putinbyte.iszhfermer.presentation.base.BaseView

interface SuccessView : BaseView {

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun setList(list: List<SuccessList>)

}