package kz.putinbyte.iszhfermer.presentation.offline.list

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.Owner
import kz.putinbyte.iszhfermer.entities.animals.AnimalSicknessModelItem
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class OfflineOwnersListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<OfflineOwnersListView>() {

    private lateinit var offlineOwnersList: List<Owners>

    override fun attachView(view: OfflineOwnersListView?) {
        super.attachView(view)
        refreshLocalData()
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            handleResult(referencesInt.getOfflineOwners(),{
                offlineOwnersList = it.data
            },{})
            updateUI()
            viewState.showLoader(false)
        }
    }

    fun updateUI() {
        if (::offlineOwnersList.isInitialized)  viewState.setList(offlineOwnersList)
    }

    fun onItemClicked(items: List<AnimalSicknessModelItem>?, position: Int) {

    }

    fun onUploadClicked() {
        viewState.showOwnersDialog()
    }

    fun showAlertRemoveDialog(items: List<Owners>?, position: Int) {
        viewState.showRemoveDialog(items,position)
    }

    fun onRemoveClicked(items: List<Owners>?, position: Int) {
        // 1. удаления из БД
        // 2. удаления из адаптера
    }

    fun onOwnerChanged(items: Owners) {
        router.navigateTo(Screens.DetailAddOwners(items))
    }
}