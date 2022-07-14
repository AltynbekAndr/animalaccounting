package kz.putinbyte.iszhfermer.presentation.diagnostic.list

import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class DiagnosticListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor
) : BasePresenter<DiagnosticListView>() {

    var animalId:Int? = null

    fun onButtonClicked(animalId:Int?) {
        router.navigateTo(Screens.Diagnostic(animalId))
    }

}