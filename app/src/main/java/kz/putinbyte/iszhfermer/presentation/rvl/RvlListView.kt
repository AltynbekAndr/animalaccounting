package kz.putinbyte.iszhfermer.presentation.rvl

import kz.putinbyte.iszhfermer.entities.animals.ListAnimalsModel
import kz.putinbyte.iszhfermer.entities.animals.TypeAnimalModel
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView

interface RvlListView : BaseView {
    @AddToEndSingle
    fun setGettingListAnimals(list: ListAnimalsModel?)

    @AddToEndSingle
    fun setListKato(list: List<Kato>)

    @AddToEndSingle
    fun setKatoRegion(list: List<Kato>)

    @AddToEndSingle
    fun setKatoCite(list: List<Kato>)

    @AddToEndSingle
    fun setTypeAnimal(list: List<TypeAnimalModel>)

    @AddToEndSingle
    fun setSuccessfulLoading(areas: Boolean? = false, animals: Boolean? = false, list: Boolean? = false)

    @AddToEndSingle
    fun setErrorMessage(text: String)

    @AddToEndSingle
    fun errorMessage()
}