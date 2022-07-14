package kz.putinbyte.iszhfermer.presentation.search.dialogRegions

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.LogUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class DialogRegionsListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor,
    private val commonDataInteractor: CommonDataInteractor
) : BasePresenter<DialogRegionsView>() {

    val TAG = javaClass.simpleName

    override fun attachView(view: DialogRegionsView?) {
        super.attachView(view)
        setFirstRegionList()
    }

     fun setFirstRegionList() {
         viewState.showLoader(true)
        launch {
            try {
                val result = referencesInteractor.getRegisteredAnimals()

                if (result?.body()?.animalAmountByKatos.isNullOrEmpty()){
                    viewState.showMessage(result?.message().toString())
                }else{
                    viewState.setList(result?.body()!!)
                }
                viewState.showLoader(false)
            }catch (e: Exception){
                LogUtils.error(TAG, e.message)
            }
        }
    }

    fun setNextRegionList(kato: Long) {
        viewState.showLoader(true)
        launch {
            try {
                val result = referencesInteractor.getRegisteredAnimalsByKato(kato)
                if (result?.body()?.animalAmountByKatos.isNullOrEmpty()){
                    viewState.showMessage(result?.errorBody().toString())
                }else{
                    viewState.setList(result?.body()!!)
                }
                viewState.showLoader(false)
            } catch (e: Exception) {
                LogUtils.error(TAG, e.message)
            }
        }
    }

}