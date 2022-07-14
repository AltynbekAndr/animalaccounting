package kz.putinbyte.iszhfermer.presentation.diagnostic.success

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import javax.inject.Inject

class SuccessPresenter @Inject constructor(
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<SuccessView>() {

    var animalId:Int? = null

    override fun attachView(view: SuccessView?) {
        super.attachView(view)
        loadSuccess()
    }

    private fun loadSuccess(){
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInteractor.getSuccessById(it)
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()){
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