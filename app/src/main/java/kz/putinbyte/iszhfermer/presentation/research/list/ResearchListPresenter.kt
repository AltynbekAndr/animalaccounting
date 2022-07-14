package kz.putinbyte.iszhfermer.presentation.research.list

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class ResearchListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<ResearchListView>() {

    var animalId: Int? = null

    override fun attachView(view: ResearchListView?) {
        super.attachView(view)
        loadResearchList()
    }

    fun loadResearchList() {
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInt.getAnimalResearchById(it, 2)
                val causeResult = referencesInt.getResearchResult()
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()){
                        res.data.forEach { animal->
                            animal.causeResult = causeResult.firstOrNull{it.value == animal.cause}?.nameRu
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

    fun onButtonClicked(animalId: ArrayList<Int>) {
        router.navigateTo(Screens.Research(animalId))
    }

    fun onItemClicked(items: List<AnimalResearchModelItem>?, position: Int) {

    }
}