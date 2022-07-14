package kz.putinbyte.iszhfermer.presentation.diagnostic.canceled

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CanceledListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager,
) : BasePresenter<CanceledView>() {

    var animalId: Int? = null

    override fun attachView(view: CanceledView?) {
        super.attachView(view)
        loadCanceledList()
    }

    private fun loadCanceledList() {
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInteractor.getCanceledById(it)
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()) {
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

}
