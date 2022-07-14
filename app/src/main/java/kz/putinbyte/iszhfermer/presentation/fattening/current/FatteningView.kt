package kz.putinbyte.iszhfermer.presentation.fattening.current

import kz.putinbyte.iszhfermer.entities.BaseFormat
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface FatteningView : BaseView {

    @AddToEndSingle
    fun showCountryType(list: List<BaseFormat>)

    @AddToEndSingle
    fun showFatteningSquareType(list: List<BaseFormat>)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, idName:String)

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun disableKato()

    @AddToEndSingle
    fun eneabled()

}