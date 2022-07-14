package kz.putinbyte.iszhfermer.presentation.ownersList

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.LogUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class OwnersListPresenter @Inject constructor(
    private val router: Router,
    private val iszhInteractor: RegAnimalInteractor,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<OwnersListView>() {

    var katoId: Int? = null
    val TAG = javaClass.simpleName

    override fun attachView(view: OwnersListView?) {
        super.attachView(view)
        loadOwners()
        katoId?.let {
            iszhInteractor.katoId = it
        }
    }

    private fun loadOwners() {
        viewState.showProgressBar(true)
        launch {
            try {
                katoId?.let {
                    val result = referencesInteractor.getOwnersByKato(it)
                    if (result?.lists?.isNotEmpty()!!){
                        viewState.setList(result.lists)
                        viewState.showProgressBar(false)
                    }else{
                        viewState.showProgressBar(false)
                        viewState.showMessage(rm.getString(R.string.noData))
                    }

                }

            } catch (e: Exception) {
                LogUtils.error(TAG, e.message)
                viewState.showProgressBar(false)
            }
        }
    }

    fun onAnimalListClicked(katoId: Int, ownerId: Int) {
        router.navigateTo(Screens.IszhList(katoId, ownerId))
    }

    fun onOwnersClicked() {
        router.navigateTo(Screens.Individuals)
    }
}