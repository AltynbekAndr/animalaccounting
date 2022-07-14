package kz.putinbyte.iszhfermer.presentation.fattening.list

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class FatteningListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<FatteningListView>() {

    var animalId: Int? = null

    override fun attachView(view: FatteningListView?) {
        super.attachView(view)
        loadFatteining()
    }

    private fun loadFatteining() {
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInteractor.getAnimalFatteningById(it)
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()){
                        res.data.forEach { animal ->
                            animal.inFattening =
                                if (animal.removeCauseId == "finish_Feeding") "Завершение откорма" else if (animal.removeCauseId == "re_feeding") "" else "Изменение откормочной площадки"
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

    fun onButtonClicked() {
        animalId?.let {
            router.navigateTo(Screens.Fattening(it))
        }
    }


}