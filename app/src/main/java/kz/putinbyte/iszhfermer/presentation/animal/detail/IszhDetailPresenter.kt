package kz.putinbyte.iszhfermer.presentation.animal.detail

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.animals.AnimalDetail
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.MyUtils
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class IszhDetailPresenter @Inject constructor(
    val router: Router,
    val regAnimalInt: RegAnimalInteractor,
    val referencesInteractor: ReferencesInteractor,
    private val commonDataInteractor: CommonDataInteractor
) : BasePresenter<IszhDetailView>() {

    var animalId: Int? = null
    var animalDetail = AnimalDetail()
    lateinit var currentAnimal: RegAnimal

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAnimalDetails()
    }

    override fun attachView(view: IszhDetailView?) {
        super.attachView(view)

    }

    private fun loadAnimalDetails() {
        viewState.setIsLoading(true)
        launch {
            try {
                val result = regAnimalInt.getAnimalInfoById(animalId!!)
                val allDirections = referencesInteractor.getAllDirection()
                val kindOfAnimal = referencesInteractor.getKindsOfAnimals()
                val allMast = referencesInteractor.getAnimalAllMast()
                val genders = commonDataInteractor.gender
                val identities = referencesInteractor.getTypeIdentifications()
                val country = referencesInteractor.getCountries()

                handleResult(result, { res ->
                    launch {

                        currentAnimal = res.data

                        animalDetail.kato =  referencesInteractor.getKatoById(currentAnimal.katoId)?.nameRu

                        animalDetail.registrationDate = if (currentAnimal.registrationDate != null) MyUtils.toMyDate(currentAnimal.registrationDate) else "_"

                        animalDetail.animalKindOfAnimal =
                            kindOfAnimal.firstOrNull { it.id == currentAnimal.animalKindId }?.nameRu
                                ?: "_"
                        animalDetail.motherInj = currentAnimal.motherInj ?: "_"
                        animalDetail.direction =
                            allDirections?.firstOrNull { it.id == currentAnimal.directionId }?.nameRu
                                ?: "_"
                        animalDetail.mastItem =
                            allMast.firstOrNull { it.id == currentAnimal.mastId }?.nameRu ?: "_"
                        animalDetail.gender =
                            genders.firstOrNull { it.id == currentAnimal.genderId }?.nameRu
                        animalDetail.identity =
                            identities.firstOrNull { it.id == currentAnimal.animalKindId }?.nameRu
                                ?: "_"
                        animalDetail.importCountry =
                            country.firstOrNull { it.id == currentAnimal.countryId }?.nameRu ?: "_"
                        animalDetail.import = if (currentAnimal.import) "Да" else "Нет"
                        animalDetail.isBreed = if (currentAnimal.isBreed) "Да" else "Нет"
                        animalDetail.animalKindOfAnimal =
                            kindOfAnimal.firstOrNull { it.id == currentAnimal.animalKindId }?.nameRu
                                ?: "_"
                        animalDetail.owner = currentAnimal.fullName ?: ""
                        val date = MyUtils.toMyDate(currentAnimal.birthDate)
                        animalDetail.birthDate = date
                        animalDetail.comment = currentAnimal.comment
                        viewState.showAnimalData(animalDetail)
                        viewState.setIsLoading(false)
                    }
                }) {
                    //viewState.back()
                    viewState.showAnimalData(animalDetail)
                    viewState.setIsLoading(false)

                }

            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
    }

    fun onInjClicked(animalid: Int) {
        router.navigateTo(Screens.IssuedInjList(animalid))
    }


    fun onSicknessesClicked(animalid: Int) {
        router.navigateTo(Screens.SicknessesList(animalid))
    }

    fun onEditClicked() {
        launch {
            if (::currentAnimal.isInitialized) {
                router.navigateTo(Screens.RegisterAnimal(animalId = currentAnimal.id))
            }
        }

    }

    fun onDeleteClicked() {
        launch {
            if (::currentAnimal.isInitialized) {
                regAnimalInt.deleteAnimal(currentAnimal)
                viewState.showMessage("Успешно удалено!")
                router.navigateTo(Screens.OfflineAnimalsList)
            }
        }
    }

    fun onResearchClicked(animalid: Int) {
        router.navigateTo(Screens.ResearchList(animalid))
    }

    fun onSourceClicked(animalid: Int) {
        router.navigateTo(Screens.PreventionList(animalid))
    }

    fun onDepositListClicked(animalid: Int) {
        router.navigateTo(Screens.DepositList(animalid))
    }

    fun onFatteningListClicked() {
        animalId?.let {
            router.navigateTo(Screens.FatteningList(it))
        }
    }

    fun onHistoryClicked() {
        animalId?.let {
            router.navigateTo(Screens.HistoryList(it))
        }
    }

    fun onDiagnosticClicked() {
        animalId?.let {
            router.navigateTo(Screens.DiagnosticList(it))
        }
    }
}