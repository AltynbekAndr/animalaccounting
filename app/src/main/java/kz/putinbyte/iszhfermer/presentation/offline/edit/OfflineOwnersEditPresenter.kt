package kz.putinbyte.iszhfermer.presentation.offline.edit

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class OfflineOwnersEditPresenter @Inject constructor(
    private val router: Router,
    private val regAnimalInt: RegAnimalInteractor,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<OfflineOwnersEditView>() {

    var curOwner = Owners()

    var validateError: Boolean = false

    private lateinit var animalKindCache: List<KindOfAnimal>

    override fun attachView(detailView: OfflineOwnersEditView?) {
        super.attachView(detailView)
        refreshLocalData()

    }

    private fun updateUI() {
        viewState.visibleReset(false)
        viewState.showData(curOwner)
        if (::animalKindCache.isInitialized) BaseFormat.toFormat(animalKindCache)
            ?.let { viewState.showAnimalKind(it) }
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            animalKindCache = referencesInt.getKindsOfAnimals()
            updateUI()
            viewState.showLoader(false)
        }
    }

    fun onSaveClicked() {
        launch {
            validate(curOwner)
            if (!validateError) {
                viewState.showLoader(true)
//                    val result = regAnimalInt.setOfllineOwners(saveOwners)
//                    handleResult(result, {
//                        viewState.showLoader(false)
//                        viewState.showMessage(rm.getString(R.string.success))
//                        router.exit()
//                    }) {
//                        handleError2(it, rm) { msg ->
//                            viewState.showLoader(false)
//                            viewState.showMessage(msg)
//                        }
//                    }
            } else
                viewState.showMessage(rm.getString(R.string.fill_all_fields))
        }

    }

    private fun validate(saveOwners: Owners) {
        viewState.resetError()
        viewState.showValidateError(saveOwners.clearDate.isNullOrEmpty(), "Date")
    }

}