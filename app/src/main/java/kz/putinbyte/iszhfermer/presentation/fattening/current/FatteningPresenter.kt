package kz.putinbyte.iszhfermer.presentation.fattening.current

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.fattening.Fattening
import kz.putinbyte.iszhfermer.entities.animals.fattening.Lists
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.MyUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class FatteningPresenter @Inject constructor(
    private val router: Router,
    private val iszhInteractor: RegAnimalInteractor,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<FatteningView>() {

    val fattening = Fattening()
    var animalId: Int? = null
    var validateError: Boolean = false

    private lateinit var countriesCache: List<Country>
    private lateinit var squareCache: List<Lists>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        animalId?.let {
            fattening.animalId = it
        }
        refreshLocalData()
    }

    fun updateUI() {
        viewState.visibleReset(false)
        if (::countriesCache.isInitialized) BaseFormat.toFormat(countriesCache)
            ?.let { viewState.showCountryType(it) }
        if (::squareCache.isInitialized) BaseFormat.toFormat(squareCache)
            ?.let { viewState.showFatteningSquareType(it) }
    }

    override fun attachView(view: FatteningView?) {
        super.attachView(view)
        updateUI()
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            countriesCache = referencesInteractor.getCountries()
            squareCache = referencesInteractor.getFatteningSquares()?.lists!!
            updateUI()
            viewState.showLoader(false)
        }
    }

    fun onSaveClicked() {
        launch {
            validate(fattening)
            if (!validateError) {
                viewState.showLoader(true)
                val result = iszhInteractor.setFattening(fattening)
                if (result?.isSuccessful!! && result.body() != null) {
                    viewState.showLoader(false)
                    viewState.showMessage(rm.getString(R.string.success))
                    router.exit()
                } else {
                    viewState.showLoader(false)
                    viewState.showMessage(result.message())
                }
            }else
                viewState.showMessage(rm.getString(R.string.fill_all_fields))
        }
    }

    private fun validate(fattening: Fattening) {
        viewState.resetError()
        viewState.showValidateError(fattening.startDate.isEmpty(), "Date")
        viewState.showValidateError(fattening.fatteningAreaId == 0, "Area")

    }

    fun onDisabled(checked: Boolean) {
        if (checked)
            viewState.disableKato()
        else
            viewState.eneabled()
    }

    fun onDateChanged(format: String) {
        val date = MyUtils.tuServerDate(format)
        fattening.startDate = date
    }

}