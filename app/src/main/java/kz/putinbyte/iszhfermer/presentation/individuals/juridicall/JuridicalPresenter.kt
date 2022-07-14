package kz.putinbyte.iszhfermer.presentation.individuals.juridicall

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.individuals.Juridical
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class JuridicalPresenter @Inject constructor(
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager,
    private val router: Router
) : BasePresenter<JuridiicalView>() {

    var validateError: Boolean = false
    var juridical = Juridical()
    private lateinit var productionCache: List<BaseFormat>
    private lateinit var propertyCache: List<BaseFormat>
    private lateinit var enterpriseCache: List<BaseFormat>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    private fun updateUI() {
        viewState.visibleReset(false)
        if (::productionCache.isInitialized) BaseFormat.toFormat(productionCache)
            ?.let { viewState.showProduction(it) }
        if (::propertyCache.isInitialized) BaseFormat.toFormat(propertyCache)
            ?.let { viewState.showPropertyType(it) }
        if (::enterpriseCache.isInitialized) BaseFormat.toFormat(enterpriseCache)
            ?.let { viewState.showEnterpriseType(it) }

    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            try {
                productionCache = referencesInteractor.getPruction()
                propertyCache = referencesInteractor.getProperty()
                enterpriseCache = referencesInteractor.getEnterprise()
                updateUI()
                viewState.showLoader(false)
            }catch (e:Exception){
                viewState.showMessage(e.message.toString())
                viewState.showLoader(false)
            }

        }
    }

    override fun attachView(view: JuridiicalView?) {
        super.attachView(view)
        updateUI()
    }

    fun onSaveClicked() {
        launch {
            try {
                validate(juridical)
                if (!validateError) {
                    viewState.showLoader(true)
                    val result = referencesInteractor.setJuridical(juridical)
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

    private fun validate(juridical: Juridical) {
        viewState.resetError()
        viewState.showValidateError(juridical.bin.isEmpty(), "bin")
        viewState.showValidateError(juridical.nameRu.isEmpty(), "fio")
        viewState.showValidateError(juridical.shortNameRu.isEmpty(), "shortName")
        viewState.showValidateError(juridical.opfId == 0, "property")
        viewState.showValidateError(juridical.okedId == 0, "production")
        viewState.showValidateError(juridical.email.isEmpty(), "email")
        viewState.showValidateError(juridical.documentDateIssue.isEmpty(), "date")
        viewState.showValidateError(juridical.katoId == 0, "kato")
    }
}