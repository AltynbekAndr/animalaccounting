package kz.putinbyte.iszhfermer.presentation.sicknesses.current

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import kz.putinbyte.iszhfermer.entities.network.DoctorType
import kz.putinbyte.iszhfermer.entities.sickness.GroupAnimalSickness
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessCauseItem
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SicknessesPresenter @Inject constructor(
    private val router: Router,
    private val regAnimalInt: RegAnimalInteractor,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<SicknessesView>() {

    var animalsId: ArrayList<Int>? = null
    val sickness = AnimalSickness()

    var validateError: Boolean = false

    private lateinit var sicknessesCache: List<SicknessKindItem>
    private lateinit var sicknessCauseCache: List<SicknessCauseItem>
    private lateinit var doctorTypeCache: DoctorType

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    override fun attachView(view: SicknessesView?) {
        super.attachView(view)
        updateUI()
    }

    private fun updateUI() {
        viewState.visibleReset(false)
        if (::sicknessesCache.isInitialized) BaseFormat.toFormat(sicknessesCache)
            ?.let { viewState.showFirstDiagnosisType(it) }
        if (::sicknessCauseCache.isInitialized)
            BaseFormat.toFormat(
                items = sicknessCauseCache,
                id = "value",
                code = "text"
            )?.let { viewState.showConclusionType(it) }
        if (::doctorTypeCache.isInitialized) BaseFormat.fromUserData(doctorTypeCache.lists)
            ?.let { viewState.showDoctorType(it) }
        viewState.showSicknessData(sickness)
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            sicknessesCache = referencesInt.getSicknesses()
            sicknessCauseCache = referencesInt.getSicknessCause()
            doctorTypeCache = referencesInt.getDoctorType()
            updateUI()
            viewState.showLoader(false)
        }
    }

    fun onSaveClicked() {
        launch {
            animalsId?.let {  ids ->
                validate(sickness)
                val animalsSickness = ids.map {sickness.copy(animalId = it)}
                val data = GroupAnimalSickness(animalSickness = animalsSickness)
                if (!validateError) {
                    viewState.showLoader(true)
                    val result = regAnimalInt.setSickness(data)
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
                }else
                    viewState.showMessage(rm.getString(R.string.fill_all_fields))
            }
        }

    }
    private fun validate(sicknessData: AnimalSickness) {
        viewState.resetError()
        viewState.showValidateError(sicknessData.initialDiagnosisId == 0 , "Diagnostic")
        viewState.showValidateError(sicknessData.sicknessCause == 0, "Conclusion")
        viewState.showValidateError(sicknessData.doctorId == 0, "Doctor")
        viewState.showValidateError(sicknessData.sicknessRegDate.isEmpty(), "Date")
    }

}