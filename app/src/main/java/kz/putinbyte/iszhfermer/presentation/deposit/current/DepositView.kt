package kz.putinbyte.iszhfermer.presentation.deposit.current

import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface DepositView : BaseView {

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, idName:String)

    @Skip
    fun showLoader(show:Boolean)
}