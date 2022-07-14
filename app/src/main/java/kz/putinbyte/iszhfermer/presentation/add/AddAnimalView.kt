package kz.putinbyte.iszhfermer.presentation.add

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface AddAnimalView : BaseView {

    @AddToEndSingle
    fun showCauseRegistration(items: List<BaseFormat>)

    @AddToEndSingle
    fun showKato(items: List<BaseFormat>)

    @AddToEndSingle
    fun showKindOfAnimal(items: List<BaseFormat>)

    @AddToEndSingle
    fun showMrsTypeSpinner(items: List<BaseFormat>)

    @AddToEndSingle
    fun showAllDirections(items: List<BaseFormat>)

    @AddToEndSingle
    fun showMastTypes(items: List<BaseFormat>)

    @Skip
    fun showErrorMessage(message: String)

    @AddToEndSingle
    fun showCountryOrigin(result: List<BaseFormat>)

    @AddToEndSingle
    fun visibleReset(visible : Boolean)

    @AddToEndSingle
    fun showTypeIdentification(items: List<BaseFormat>)

    @AddToEndSingle
    fun showGenderAnimal(items: List<BaseFormat>)

    @AddToEndSingle
    fun showDateOfBirthday(date: String?)

    @AddToEndSingle
    fun showDatePickerDialog(isStart:Boolean,minDay:Int,maxDay:Int)

    @AddToEndSingle
    fun setLoadingState(show: Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showAnimalData(animal: RegAnimal)

    @AddToEndSingle
    fun showValidateError(error: Boolean, fieldKey:String)

    @AddToEndSingle
    fun showMrsSpinner(b: Boolean)

    @AddToEndSingle
    fun setOwnerInn(ownerInn: Owners?)


}