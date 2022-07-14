package kz.putinbyte.iszhfermer.presentation.animal.detail

import kz.putinbyte.iszhfermer.entities.animals.AnimalDetail
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView

interface IszhDetailView : BaseView {

    @AddToEndSingle
    fun showAnimalData(animal: AnimalDetail)

    @AddToEndSingle
    fun setIsLoading(isLoading: Boolean)
}