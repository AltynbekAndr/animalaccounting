package kz.putinbyte.iszhfermer.presentation.prevention.list

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.animals.AnimalPreventiveActionModelItem
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class PreventionListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<PreventionListView>() {

    var animalId: Int? = null
    val immunModel = AnimalPreventiveActionModelItem()

    override fun attachView(view: PreventionListView?) {
        super.attachView(view)
        loadPreventionList()
    }

    private fun loadPreventionList() {
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInt.getAnimalPreventionById(it, 2)
                val immunKind = referencesInt.getImmunKind()
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()){
                        res.data.forEach {
                                immunId-> immunId.immunKind = immunKind.firstOrNull{it.id == immunId.immunKindId }?.nameRu
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

    fun onItemClicked(items: List<AnimalPreventiveActionModelItem>?, position: Int) {

    }

    fun onAddClicked(animalId: Int) {
        animalId.let {
        router.navigateTo(Screens.Prevention(arrayListOf(it)))
        }
    }
}