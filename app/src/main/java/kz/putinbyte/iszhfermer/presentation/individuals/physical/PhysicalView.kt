package kz.putinbyte.iszhfermer.presentation.individuals.physical

import kz.putinbyte.iszhfermer.entities.BaseFormat
import moxy.viewstate.strategy.alias.Skip
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface PhysicalView : BaseView {

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun showCountryOrigin(result: List<BaseFormat>)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, fieldKey:String)

    @AddToEndSingle
    fun showPropertyType(propertyType:List<BaseFormat>)

    @AddToEndSingle
    fun onTypeChanged(id:Int)

    @Skip
    fun getEditLength(it: String)

}