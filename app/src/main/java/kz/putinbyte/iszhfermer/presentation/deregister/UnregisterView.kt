package kz.putinbyte.iszhfermer.presentation.deregister

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.unregister.Unregister
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface UnregisterView : BaseView {

    @AddToEndSingle
    fun setCauseList(list: List<BaseFormat>)

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun showData(unregister: Unregister)

    @AddToEndSingle
    fun showVisibility()

    @AddToEndSingle
    fun showSicknessType(items: List<BaseFormat>)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, fieldKey:String)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)


}