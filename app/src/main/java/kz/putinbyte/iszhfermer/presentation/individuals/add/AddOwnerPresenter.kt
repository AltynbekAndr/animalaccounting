package kz.putinbyte.iszhfermer.presentation.individuals.add

import android.net.Uri
import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.entities.animals.AnimalMastItem
import kz.putinbyte.iszhfermer.entities.animals.individuals.Physical
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.IResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class AddOwnerPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val regAnimalInt: RegAnimalInteractor,
    private val commonDataInteractor: CommonDataInteractor,
    private val rm: IResourceManager,
) : BasePresenter<AddOwnersView>() {

    var ownerId: Int? = null
    var ownerInfo: Owners? = null
    var katoId: Int? = null
    var animalId: Int? = null

    val animalBook = AnimalBook()
    var currentAnimal = RegAnimal()
    var physical = Physical()
    var validateError: Boolean = false
    var isConnect = true

    private lateinit var kindOfAnimalCache: List<KindOfAnimal>
    private lateinit var identityCache: List<Identity>
    private lateinit var countriesCache: List<Country>
    private lateinit var gendersCache: List<Gender>
    private lateinit var registrationCache: List<Registration>
    private lateinit var katoCache: List<Kato>
    private lateinit var directionCache: List<BaseFormat>
    private lateinit var animalMastCache: List<AnimalMastItem>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        ownerId?.let {
            currentAnimal.ownerId = it
        }
        katoId?.let {
            currentAnimal.katoId = it
            regAnimalInt.ownerId = it
            regAnimalInt.katoId = it

        }
        animalId?.let { anId ->
            launch {
                val getAnimalRes = regAnimalInt.getAnimalInfoById(anId)
                handleResult(getAnimalRes, {
                    currentAnimal = it.data
                }) {}
            }
        }
        refreshLocalData()
    }

    private fun updateUI() {
        viewState.visibleReset(false)
        syncByScanData()
        if (::identityCache.isInitialized) BaseFormat.toFormat(identityCache)
            ?.let { viewState.showProduction(it) }
        if (::kindOfAnimalCache.isInitialized) BaseFormat.toFormat(kindOfAnimalCache)
            ?.let { viewState.showKindOfAnimal(it) }
//        if (::countriesCache.isInitialized) BaseFormat.toFormat(countriesCache)
//            ?.let { viewState.showCountryOrigin(it) }
        if (::gendersCache.isInitialized) BaseFormat.toFormat(gendersCache)
            ?.let { viewState.showGenderAnimal(it) }
        if (::registrationCache.isInitialized) BaseFormat.toFormat(registrationCache)
            ?.let { viewState.showCauseRegistration(it) }
        if (::katoCache.isInitialized) BaseFormat.toFormat(katoCache)
            ?.let { viewState.showKato(it) }
        viewState.showAnimalData(currentAnimal)
    }

    private fun refreshLocalData() {
        viewState.setLoadingState(true)
        launch {
            kindOfAnimalCache = referencesInt.getKindsOfAnimals()
            identityCache = referencesInt.getTypeIdentifications()
//            countriesCache = referencesInt.getCountries()
            gendersCache = commonDataInteractor.gender
            registrationCache = commonDataInteractor.registration
            katoCache = referencesInt.getRegions()
//            ownerId?.let {
//                ownerInfo = referencesInt.getOwnersById(it)
//            }
            val owners = referencesInt.getOwnersById(ownerId!!)
            handleResult(owners,{
                ownerInfo = it.data
            }){}
            updateUI()
            viewState.setLoadingState(false)
        }
    }

    private fun syncByScanData() {
        regAnimalInt.scanData?.let { scanData ->
            scanData.animalKind?.let { currentAnimal.animalKindId = it.id }
            scanData.inj?.let { currentAnimal.inj = it }
            scanData.typeIdentification?.let { currentAnimal.identId = it.id }
            scanData.causeRegistration?.let { currentAnimal.causeId = it.id }
            scanData.kato?.let { currentAnimal.katoId = it.id }
            scanData.country?.let { currentAnimal.countryId = it.id }
        }
        regAnimalInt.scanData = null
    }

    override fun attachView(view: AddOwnersView?) {
        super.attachView(view)
        updateUI()
    }

    fun onScannerClicked() {
        router.navigateTo(Screens.Scanner)
    }

    fun loadDirections() {
        launch {
            directionCache = referencesInt.getDirectionById(currentAnimal.animalKindId)
            if (::directionCache.isInitialized) BaseFormat.toFormat(directionCache)
                ?.let { if (!it.isNullOrEmpty()) viewState.showAllDirections(it) }
        }
    }

    fun loadMastAnimal() {
        launch {
            val firstMast =
                if (::animalMastCache.isInitialized) animalMastCache.firstOrNull() else null
            if (
                firstMast?.animalKindId != currentAnimal.animalKindId ||
                firstMast.directionId != currentAnimal.directionId
            ) {
                animalMastCache = referencesInt.getAnimalMast(
                    currentAnimal.animalKindId,
                    currentAnimal.directionId
                )
            }
            if (::animalMastCache.isInitialized) BaseFormat.toFormat(animalMastCache)
                ?.let { viewState.showMastTypes(it) }
        }
    }

    fun onOwnerChanged(ownerId: Int? = null, ownerINN: String? = null) {
        when {
            ownerId != null -> {
                currentAnimal.ownerId = ownerId
                currentAnimal.ownerINN = null
            }
            ownerINN != null -> {
                currentAnimal.ownerINN = ownerINN
                currentAnimal.ownerId = null
            }
        }
    }


    fun onGreenDateChanged(format: String?) {
        animalBook.date = format
    }

    fun onSaveClicked() {
        launch {
            try {
                validate(currentAnimal)
                if (!validateError) {
                    viewState.setLoadingState(true)
                    val result = regAnimalInt.setAnimals(currentAnimal)
                    handleResult(result, {
                        viewState.setLoadingState(false)
                        viewState.showMessage(rm.getString(R.string.success))
                        router.exit()
                    }) {
                        handleError2(it, rm) { msg ->
                            viewState.setLoadingState(false)
                            viewState.showErrorMessage(msg)
                        }
                    }

                } else
                    viewState.showErrorMessage(rm.getString(R.string.fill_all_fields))

            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
    }

    private fun validate(animal: RegAnimal) {

        viewState.resetError()

        viewState.showValidateError(
            ::animalMastCache.isInitialized && animal.mastId == 0 && !animalMastCache.isNullOrEmpty(),
            "Mast"
        )
        viewState.showValidateError(
            ::directionCache.isInitialized && animal.directionId == 0 && !directionCache.isNullOrEmpty(),
            "Direction"
        )
        if (isConnect)
            viewState.showValidateError(animal.ownerId == 0, "Owner")
        else
            viewState.showValidateError(animal.ownerINN.isNullOrEmpty(), "OwnerOffline")
        viewState.showValidateError(animal.birthDate.isEmpty(), "BirthDate")
        viewState.showValidateError(animal.inj.isEmpty(), "Inj")
        viewState.showValidateError(animal.animalKindId == 0, "AnimalKindS")
        viewState.showValidateError(animal.identId == 0, "Identification")
        viewState.showValidateError(animal.katoId == 0, "Kato")
        viewState.showValidateError(animal.countryId == 0, "Country")
        viewState.showValidateError(animal.causeId == 0, "CauseRegistry")
    }

    suspend fun onUploadFileClicked(uri: Uri) {
        val result = regAnimalInt.saveSelectedFile(uri)

        //animalBook.files = listOf(File(uri.path!!))
    }


    fun onPermissionDenied(string: String) {
        viewState.showMessage(string)
    }

    fun onCauseRegistryChanged(items: List<BaseFormat>, position: Int) {
        val list = ArrayList<Country>()
        when {
            items[position].nameRu.equals("Приплод") -> {
                launch {
                    var result = referencesInt.getCountries()
                    val newCountry = result.firstOrNull { it.id == 64 }
                    if (newCountry != null)
                        list.add(newCountry)
                    result = list
                    BaseFormat.toFormat(result)
                        ?.let {
                            viewState.showCountryOrigin(it)
                        }
                }
            }
            items[position].nameRu.equals("Приобретение(импорт)") -> {
                launch {
                    var result = referencesInt.getCountries()
                    list.addAll(result)
                    val index = result.indexOfFirst { it.id == 64 }
                    list.removeAt(index)
                    BaseFormat.toFormat(list)
                        ?.let { viewState.showCountryOrigin(it) }
                }

            }
            else -> {
                launch {
                    val result = referencesInt.getCountries()
                    BaseFormat.toFormat(result)
                        ?.let { viewState.showCountryOrigin(it) }
                }
            }
        }
    }

}