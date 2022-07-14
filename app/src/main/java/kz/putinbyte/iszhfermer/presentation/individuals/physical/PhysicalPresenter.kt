package kz.putinbyte.iszhfermer.presentation.individuals.physical

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.individuals.Physical
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.requests.PropertyType
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class PhysicalPresenter @Inject constructor(
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager,
    private val router: Router
) : BasePresenter<PhysicalView>() {

    var physical = Physical()
    var validateError: Boolean = false
    private lateinit var countriesCache: List<Country>
    private lateinit var propertyTypeCache: List<PropertyType>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    private fun updateUI() {
        viewState.visibleReset(false)
        if (::countriesCache.isInitialized) BaseFormat.toFormat(countriesCache)
            ?.let { viewState.showCountryOrigin(it) }

        if (::propertyTypeCache.isInitialized) BaseFormat.toFormat(propertyTypeCache)
            ?.let { viewState.showPropertyType(it) }
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            try {
                countriesCache = referencesInt.getCountries()
                val typeProperty = referencesInt.getTypeProperty()
                handleResult(typeProperty, { result ->
                    propertyTypeCache = result.data
                }) { error ->
                    viewState.showMessage(error.exception.message.toString())
                }

                updateUI()
                viewState.showLoader(false)
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
                viewState.showMessage(e.message.toString())
                viewState.showLoader(false)
            }
        }
    }

    override fun attachView(view: PhysicalView?) {
        super.attachView(view)
        updateUI()
    }

    fun onSaveClicked() {
        launch {
            try {
                validate(physical)
                if (!validateError) {
                    viewState.showLoader(true)
                    val result = referencesInt.setPhysical(physical)
                    handleResult(result, { res ->
                        viewState.showLoader(false)
                        viewState.showMessage((res.data.title))
                        router.exit()
                    }) {
                        handleError2(it, rm) { msg ->
                            viewState.showLoader(false)
                            viewState.showMessage(msg)
                        }
                    }

                } else
                    viewState.showMessage(rm.getString(R.string.fill_all_fields))
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
    }

    private fun validate(physical: Physical) {
        viewState.resetError()
        viewState.showValidateError(physical.iin.isEmpty(), "iin")
        viewState.showValidateError(physical.firstName.isEmpty(), "firstName")
        viewState.showValidateError(physical.lastName.isNullOrEmpty(), "lastName")
        viewState.showValidateError(physical.middleName.isNullOrEmpty(), "middleName")
        viewState.showValidateError(physical.birthDate.isEmpty(), "birthDate")
        viewState.showValidateError(physical.email.isEmpty(), "email")
        viewState.showValidateError(physical.documentNumber.isEmpty(), "documentNum")
        viewState.showValidateError(physical.documentDateIssue.isEmpty(), "date")
        viewState.showValidateError(physical.doumentIssueBy.isEmpty(), "issuedBy")
        viewState.showValidateError(physical.katoId == 0, "kato")
        viewState.showValidateError(physical.citizenshipId == 0, "Country")
    }

    fun onTypeChanged(id: Int?) {
        if (id != null)
            viewState.onTypeChanged(id)
    }

    fun getEditLength(it: String) {
        viewState.getEditLength(it)
    }

    fun onInnClicked() {
        viewState.showLoader(true)
        // Запрос на сервер
        launch {
            try {
                val result = referencesInt

            }catch (e:Exception){

            }

        }

    }

}