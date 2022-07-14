package kz.putinbyte.iszhfermer.presentation.issuedInj.current

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInj
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.system.IResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class IssuedInjPresenter @Inject constructor(
    private val router: Router,
    private val regAnimalInteractor: RegAnimalInteractor,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: IResourceManager,
) : BasePresenter<IssuedInjView>() {

    var animalId: Int? = null
    val replaceInj = ReplaceInj()
    var validateError: Boolean = false
    var oldInj: HashMap<RegAnimalInteractor.InjDecode, String>? = null
    var injDecodeError : Boolean = false

    private lateinit var identityCache: List<Identity>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        animalId?.let { anId ->
            replaceInj.animalId = anId
            launch {
                val getAnimalRes = regAnimalInteractor.getAnimalInfoById(anId)
                handleResult(getAnimalRes, {
                    oldInj = regAnimalInteractor.decodeInj(it.data.inj)
                    viewState.setBaseInjData(
                        "${oldInj?.get(RegAnimalInteractor.InjDecode.MAIN)}"
                    )
                }) {}
            }
        }
        refreshLocalData()
    }

    private fun updateUI() {
        syncByScanData()
        if (::identityCache.isInitialized) BaseFormat.toFormat(identityCache)
            ?.let { viewState.showTypeIdentification(it) }
        viewState.showData(replaceInj)
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            identityCache = referencesInteractor.getTypeIdentifications()
            updateUI()
            viewState.showLoader(false)
        }
    }

    override fun attachView(view: IssuedInjView?) {
        super.attachView(view)
        updateUI()
    }

    fun onSaveClicked() {
        launch {
            try {
                validate(replaceInj)
                if (!validateError) {
                    viewState.showLoader(true)
                    val result = referencesInteractor.setReplaceInj(replaceInj)
                    handleResult(result, {
                        viewState.showLoader(false)
                        viewState.showMessage(rm.getString(R.string.success))
                        router.exit()
                    }) {
                        handleError2(it, rm) { msg ->
                            viewState.showLoader(false)
                            viewState.showMessage(msg)
                        }
                    }
                }
                else viewState.showMessage(rm.getString(R.string.non_correct_inj_fill_all_fields))
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
                viewState.showMessage(e.message.toString())
            }
        }
    }

    private fun validate(newInj: ReplaceInj) {
        viewState.resetError()
        if (newInj.import) injDecodeError = false
        viewState.showValidateError(newInj.inj.isNullOrEmpty() || injDecodeError, "NewInj")
        viewState.showValidateError(newInj.identId == 0, "Identification")
    }

    fun setInj(inj: String) {
        if (!replaceInj.import){
            oldInj?.get(RegAnimalInteractor.InjDecode.MAIN)?.let {old ->
                val newInj = regAnimalInteractor.decodeInj(inj)
                newInj?.get(RegAnimalInteractor.InjDecode.MAIN)?.let { new ->
                    injDecodeError = old != new
                    if (injDecodeError) viewState.showMessage(rm.getString(R.string.non_correct_inj))
                }
            }
        }
        replaceInj.inj = inj
    }


    private fun syncByScanData() {
        regAnimalInteractor.scanData?.let { scanData ->
            scanData.inj?.let { replaceInj.inj = it.uppercase() }
            scanData.typeIdentification?.let { replaceInj.identId = it.id }
        }
        regAnimalInteractor.scanData = null
    }

    fun onScanClicked() {
        router.navigateTo(Screens.Scanner)
    }
}