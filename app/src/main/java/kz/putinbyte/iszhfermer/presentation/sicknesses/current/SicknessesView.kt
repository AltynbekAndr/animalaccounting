package kz.putinbyte.iszhfermer.presentation.sicknesses.current

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface SicknessesView : BaseView {

    @AddToEndSingle
    fun showConclusionType(items: List<BaseFormat>)

    @AddToEndSingle
    fun showDoctorType(items: List<BaseFormat>)

    @AddToEndSingle
    fun showFirstDiagnosisType(items: List<BaseFormat>)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, idName:String)

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun showSicknessData(sickness: AnimalSickness)
}