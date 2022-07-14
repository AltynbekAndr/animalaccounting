package kz.putinbyte.iszhfermer.presentation.rvl.create

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.DicRvlBranchModel
import kz.putinbyte.iszhfermer.entities.animals.DicRvlInstrumentModel
import kz.putinbyte.iszhfermer.entities.animals.ReceivedSampleNameModel
import kz.putinbyte.iszhfermer.entities.db.DoctorTypes
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface CreateInventoryView : BaseView {


    @AddToEndSingle
    fun showResearchType(item: List<ReceivedSampleNameModel>)

    @AddToEndSingle
    fun showResultType(item: List<DoctorTypes>)

    @AddToEndSingle
    fun showDoctorType(item: List<DicRvlBranchModel>)

    @AddToEndSingle
    fun showDiseaseType(item: List<BaseFormat>, result: List<SicknessKindItem>)

    @Skip
    fun showMaterialsType(item: List<BaseFormat>, result: List<DicRvlInstrumentModel>)

    @AddToEndSingle
    fun showAddUnitsDialog(list: List<BaseFormat>)

    @Skip
    fun initRecyclerView()

    @Skip
    fun showLoader(show:Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, idName:String)

}