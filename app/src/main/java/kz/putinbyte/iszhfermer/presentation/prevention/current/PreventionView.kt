package kz.putinbyte.iszhfermer.presentation.prevention.current

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface PreventionView : BaseView {

    @AddToEndSingle
    fun showSicknessType(items: List<BaseFormat>)

    @AddToEndSingle
    fun showDoctorTypes(items: List<BaseFormat>)

    @AddToEndSingle
    fun showImmunType(items: List<BaseFormat>)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showSicknessData(prevention: AnimalPrevention)

    @AddToEndSingle
    fun showValidateError(error: Boolean, fieldKey: String)

}