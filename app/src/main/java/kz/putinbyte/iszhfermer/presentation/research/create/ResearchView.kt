package kz.putinbyte.iszhfermer.presentation.research.create

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface ResearchView : BaseView {

    @AddToEndSingle
    fun showSicknessType(items: List<BaseFormat>)

    @AddToEndSingle
    fun showResultType(items: List<BaseFormat>)

    @AddToEndSingle
    fun showDoctorsType(items: List<BaseFormat>)

    @AddToEndSingle
    fun showResearchType(items: List<BaseFormat>)

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, idName:String)

    @AddToEndSingle
    fun showResearchData(research: AnimalResearch)

}