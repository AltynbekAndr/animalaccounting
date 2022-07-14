package kz.putinbyte.iszhfermer.presentation.sicknesses.list

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.animals.AnimalSicknessModelItem
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SicknessesListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<SicknessesListView>() {

    var animalId: Int? = null

    override fun attachView(view: SicknessesListView?) {
        super.attachView(view)
        loadResearchList()
    }

    private fun loadResearchList() {
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInt.getAnimalSicknessById(it, 2)
                val sicknessCause = referencesInt.getSicknessCause()
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()){
                        res.data.forEach {animal->
                            animal.cause = sicknessCause.firstOrNull{it.value == animal.sicknessCause}?.nameRu
                        }
                        viewState.showLoader(false)
                        viewState.setList(res.data)
                    }else{
                        viewState.showLoader(false)
                        viewState.showMessage(rm.getString(R.string.noData))
                    }


                }) {
                    handleError2(it, rm) { msg ->
                        viewState.showLoader(false)
                        viewState.showMessage(msg)
                    }
                }

            }
        }
    }

    fun onItemClicked(items: List<AnimalSicknessModelItem>?, position: Int) {
    }

    fun onAddClicked() {
        animalId?.let {
            router.navigateTo(Screens.Sicknesses(arrayListOf(it)))
        }
    }
}