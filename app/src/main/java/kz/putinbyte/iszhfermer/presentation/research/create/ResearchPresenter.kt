package kz.putinbyte.iszhfermer.presentation.research.create

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.entities.research.GroupAnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.ResearchKindItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.entities.network.DoctorType
import kz.putinbyte.iszhfermer.entities.network.ResearchResultItem
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class ResearchPresenter @Inject constructor(
    private val router: Router,
    private val regAnimalInt: RegAnimalInteractor,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<ResearchView>() {

    var animalsId: ArrayList<Int>? = null
    val research = AnimalResearch()

    var validateError: Boolean = false

    private lateinit var sicknessesCache: List<SicknessKindItem>
    private lateinit var researchKindCache: List<ResearchKindItem>
    private lateinit var researchResultCache: List<ResearchResultItem>
    private lateinit var doctorTypeCache: DoctorType


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    override fun attachView(view: ResearchView?) {
        super.attachView(view)
        updateUI()
    }

    private fun updateUI() {
        viewState.visibleReset(false)
        if (::sicknessesCache.isInitialized) BaseFormat.toFormat(sicknessesCache)
            ?.let { viewState.showSicknessType(it) }

        if (::researchKindCache.isInitialized)
            BaseFormat.toFormat(researchKindCache)?.let { viewState.showResearchType(it) }
        if (::researchResultCache.isInitialized) BaseFormat.toFormat(
            items = researchResultCache,
            id = "value",
            code = "text"
        )?.let { viewState.showResultType(it) }

        if (::doctorTypeCache.isInitialized) BaseFormat.fromUserData(doctorTypeCache.lists)
            ?.let { viewState.showDoctorsType(it) }

        viewState.showResearchData(research)
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            sicknessesCache = referencesInt.getSicknesses()
            researchKindCache = referencesInt.getResearchKind()
            researchResultCache = referencesInt.getResearchResult()
            doctorTypeCache = referencesInt.getDoctorType()
            updateUI()
            viewState.showLoader(false)
        }
    }

    fun onSaveButtonClicked() {

        launch {
            animalsId?.let { ids ->
                validate(research)
                val animalResearch = ids.map { research.copy(animalId = it) }
                val data = GroupAnimalResearch(animalResearchDtos = animalResearch)
                if (!validateError) {
                    viewState.showLoader(true)
                    val result = regAnimalInt.setResearch(data)
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

    private fun validate(sicknessData: AnimalResearch) {
        viewState.resetError()
        viewState.showValidateError(sicknessData.sicknessId == 0, "SicknessType")
        viewState.showValidateError(sicknessData.sicknessCause == 0, "Result")
        viewState.showValidateError(sicknessData.doctorId == 0, "Doctor")
        viewState.showValidateError(sicknessData.sicknessRegDate.isEmpty(), "Date")
        viewState.showValidateError(sicknessData.researchKindId == 0, "researchType")
    }
}
