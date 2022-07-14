package kz.putinbyte.iszhfermer.presentation.addresses

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.LogUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class NameRegionsListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<NameRegionsListView>() {

    val TAG = javaClass.simpleName
    var katoId: Int? = null

    override fun attachView(view: NameRegionsListView?) {
        super.attachView(view)
        setFirstRegionList()
    }

    fun setFirstRegionList() {
        viewState.showProgressBar(true)
        launch {
            try {
                val katoID = katoId?.toLong()
                val result =
                    if (katoID != null) referencesInteractor.getRegAnimalsByKato(katoID) else referencesInteractor.getRegAnimals()
                handleResult(result,{
                    viewState.showProgressBar(false)
                    viewState.setList(it.data.body()?.animalAmountByKatos!!.sortedBy { it.name })
                },{
                    handleError2(it, rm) { msg ->
                        viewState.showProgressBar(false)
                        viewState.showMessage(msg)
                    }
                })
            } catch (e: Exception) {
                LogUtils.error(TAG, e.message)
                viewState.showMessage(e.message.toString())
            }
        }
    }

    fun onAddClicked(katoId: Int) {
        router.navigateTo(Screens.RegisterAnimal(katoId = katoId))
    }

    fun setNextRegionList(kato: Long) {
        router.navigateTo(Screens.Regions(kato.toInt()))
//        viewState.showProgressBar(true)
//        launch {
//            try {
//                val result = referencesInteractor.getRegisteredAnimalsByKato(kato)
//                val region =
//                    if (result?.animalAmountByKatos.isNullOrEmpty()) commonDataInteractor.getDefaultRegion() else result
//                viewState.setList(region!!)
//                viewState.showProgressBar(false)
//            } catch (e: Exception) {
//                LogUtils.error(TAG, e.message)
//            }
//        }
    }

    fun setOwnersList(katoId: Int) {
        router.navigateTo(Screens.Owners(katoId))
    }
}