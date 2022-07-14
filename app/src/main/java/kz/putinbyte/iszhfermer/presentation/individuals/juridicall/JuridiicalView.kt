package kz.putinbyte.iszhfermer.presentation.individuals.juridicall

import kz.putinbyte.iszhfermer.entities.BaseFormat
import moxy.viewstate.strategy.alias.Skip
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface JuridiicalView : BaseView {

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, fieldKey:String)

    @AddToEndSingle
    fun showEnterpriseType(result: List<BaseFormat>)

    @AddToEndSingle
    fun showProduction(result: List<BaseFormat>)

    @AddToEndSingle
    fun showPropertyType(result: List<BaseFormat>)

}