package kz.putinbyte.iszhfermer.presentation.prevention.current

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.BaseFormatReferences
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.entities.network.DoctorType
import kz.putinbyte.iszhfermer.entities.network.GroupAnimalPrevention
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class PreventionPresenter @Inject constructor(
    private val router: Router,
    private val regAnimalInt: RegAnimalInteractor,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<PreventionView>() {

    var animalsId: ArrayList<Int>? = null
    val prevention = AnimalPrevention()

    var validateError: Boolean = false

    private lateinit var sicknessesCache: List<SicknessKindItem>
    private lateinit var immunKindCache: List<BaseFormatReferences>
    private lateinit var doctorTypeCache: DoctorType

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    override fun attachView(view: PreventionView?) {
        super.attachView(view)
        updateUI()
    }

    private fun updateUI() {
        viewState.visibleReset(false)
        if (::sicknessesCache.isInitialized) BaseFormat.toFormat(sicknessesCache)
            ?.let { viewState.showSicknessType(it) }
        if (::immunKindCache.isInitialized) BaseFormat.toFormat(immunKindCache)?.let {
            viewState.showImmunType(it)}
        if (::doctorTypeCache.isInitialized) BaseFormat.fromUserData(doctorTypeCache.lists)
            ?.let { viewState.showDoctorTypes(it) }
        viewState.showSicknessData(prevention)
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            sicknessesCache = referencesInt.getSicknesses()
            //TODO: local save
            immunKindCache = referencesInt.getImmunKind()
            doctorTypeCache = referencesInt.getDoctorType()
            updateUI()
            viewState.showLoader(false)
        }
    }

    fun onSaveClicked() {
        launch {
            animalsId?.let { ids ->
                validate(prevention)
                val animalPrevention = ids.map { prevention.copy(animalId = it) }
                val data = GroupAnimalPrevention(animalPrevention = animalPrevention)
                if (!validateError) {
                    viewState.showLoader(true)
                    val result = regAnimalInt.setPrevention(data)
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
                } else
                    viewState.showMessage(rm.getString(R.string.fill_all_fields))

            }
        }
    }

    private fun validate(prevention: AnimalPrevention) {
        viewState.resetError()
        viewState.showValidateError(prevention.sicknessId == 0, "SicknessType")
        viewState.showValidateError(prevention.immunKindId == 0, "ImmunType")
        viewState.showValidateError(prevention.doctorId == 0, "Doctor")
        viewState.showValidateError(prevention.immunizationDate.isNullOrEmpty(), "Date")
    }
}