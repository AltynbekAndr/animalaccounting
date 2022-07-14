package kz.putinbyte.iszhfermer.presentation.individuals.list

import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class IndividualsListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor
) : BasePresenter<IndividualsListView>() {

    var animalId:Int? = null

    fun onButtonClicked(animalId:Int?) {
        router.navigateTo(Screens.Diagnostic(animalId))
    }

}