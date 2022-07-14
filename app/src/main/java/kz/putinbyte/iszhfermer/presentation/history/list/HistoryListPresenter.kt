package kz.putinbyte.iszhfermer.presentation.history.list

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class HistoryListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<HistoryListView>() {

    var animalId:Int? = null

    override fun attachView(view: HistoryListView?) {
        super.attachView(view)
        loadHistory()
    }

    private fun loadHistory(){
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInteractor.getHistoryById(it)
                handleResult(result, { res ->

                    viewState.showLoader(false)
                    viewState.setList(res.data)
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