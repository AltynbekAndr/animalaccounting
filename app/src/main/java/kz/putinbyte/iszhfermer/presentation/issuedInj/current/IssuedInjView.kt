package kz.putinbyte.iszhfermer.presentation.issuedInj.current

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInj
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface IssuedInjView : BaseView {

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun showTypeIdentification(items: List<BaseFormat>)

    @AddToEndSingle
    fun setBaseInjData(inj: String?)

    @AddToEndSingle
    fun showValidateError(error: Boolean, fieldKey:String)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showData(replaceInj: ReplaceInj)
}