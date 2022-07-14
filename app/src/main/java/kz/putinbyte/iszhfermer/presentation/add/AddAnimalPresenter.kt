package kz.putinbyte.iszhfermer.presentation.add

import android.app.Activity
import android.content.Context
import android.net.Uri
import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.entities.animals.AnimalMastItem
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
import kz.putinbyte.iszhfermer.utils.MyUtils
import okhttp3.MultipartBody
import retrofit2.Response
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class AddAnimalPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val regAnimalInt: RegAnimalInteractor,
    private val commonDataInteractor: CommonDataInteractor,
    private val rm: IResourceManager,
) : BasePresenter<AddAnimalView>() {

    var ownerId: Int? = null
    var ownerInfo: Owners? = null
    var katoId: Int? = null
    var animalId: Int? = null
    val animalBook = AnimalBook()
    var currentAnimal = RegAnimal()
    var validateError: Boolean = false
    var isConnect = true
    var isDisconnect = true
    var isChip = false
    var isTV = false
    var minDay: Int = 0
    var maxDay: Int = 0

    private lateinit var kindOfAnimalCache: List<KindOfAnimal>
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
                    launch {
                        currentAnimal = it.data
                        val result = referencesInt.findOwnerByINN(currentAnimal.ownerINN?.toLong())
                        handleResult(result,{
                            val owner: Owners? = it.data.list?.firstOrNull()
                            if (owner != null) {
                                viewState.setOwnerInn(owner)
                            }
                        }){
                            error->
                            viewState.showMessage(error.exception.message.toString())
                        }
                    }
                }) {
//                    viewState.showMessage(it.exception.localizedMessage)
                }
            }
        }
        refreshLocalData()
    }

    fun updateUI() {
        viewState.visibleReset(false)
        syncByScanData()
        if (::kindOfAnimalCache.isInitialized) BaseFormat.toFormat(kindOfAnimalCache)
            ?.let { viewState.showKindOfAnimal(it) }
        if (::gendersCache.isInitialized) BaseFormat.toFormat(gendersCache)
            ?.let { viewState.showGenderAnimal(it) }
        if (::registrationCache.isInitialized) BaseFormat.toFormat(registrationCache)
            ?.let { viewState.showCauseRegistration(it) }
        if (::katoCache.isInitialized) BaseFormat.toFormat(katoCache)
            ?.let { viewState.showKato(it) }
        if (currentAnimal.birthDate.isNotEmpty()){
            val date = MyUtils.toMyBirthDate(currentAnimal.birthDate)
            currentAnimal.birthDate = date
        }
        viewState.showAnimalData(currentAnimal)
    }

    private fun refreshLocalData() {
        val list = ArrayList<Registration>()
        viewState.setLoadingState(true)
        launch {
            kindOfAnimalCache = referencesInt.getKindsOfAnimals()
            gendersCache = commonDataInteractor.gender
            val result = commonDataInteractor.registration
            val defaultList = result.firstOrNull { it.code == "Reg_Offspring" }
            list.add(defaultList!!)
            registrationCache = list
            katoCache = referencesInt.getRegions()

//            ownerId?.let {
//                ownerInfo = referencesInt.getOwnersById(it)
//            }

            val owners = referencesInt.getOwnersById(ownerId!!)
            handleResult(owners, {
                ownerInfo = it.data
            }) {}
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
            scanData.kato?.let {
                if (currentAnimal.katoId == 0) {
                    it.id
                } else {
                    katoId!!
                }

            }
            scanData.country?.let { currentAnimal.countryId = it.id }
        }
        regAnimalInt.scanData = null
    }

    override fun attachView(view: AddAnimalView?) {
        super.attachView(view)
        updateUI()
        val list = ArrayList<BaseFormat>()
        viewState.showMrsTypeSpinner(list)
    }

    fun onScannerClicked() {
        router.navigateTo(Screens.Scanner)
    }

    fun showKato() {

        launch {
            katoCache = referencesInt.getRegions()
            if (::katoCache.isInitialized) BaseFormat.toFormat(katoCache)
                ?.let { viewState.showKato(it) }
        }


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
        currentAnimal.comment = ""
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

        if (isConnect)
            viewState.showValidateError(animal.ownerId == 0, "Owner")
        else
            viewState.showValidateError(animal.ownerINN.isNullOrEmpty(), "OwnerOffline")

        viewState.showValidateError(animal.inj.isEmpty(), "Inj")

        val inj = animal.inj.toCharArray()
        viewState.showValidateError(animal.animalKindId == 8, "AnimalKindS")
        viewState.showValidateError(animal.animalKindId == 1 && inj[3] != '1', "Inj")
        viewState.showValidateError(animal.animalKindId == 5 && inj[3] != '4', "Inj")
        viewState.showValidateError(animal.animalKindId == 6 && inj[3] != '3', "Inj")
        viewState.showValidateError(animal.animalKindId == 7 && inj[3] != '5', "Inj")
        viewState.showValidateError(animal.animalKindId == 9 && inj[3] != '2', "Inj")
        viewState.showValidateError(animal.animalKindId == 10 && inj[3] != '2', "Inj")
        viewState.showValidateError(animal.animalKindId == 11 && inj[3] != '6', "Inj")
        viewState.showValidateError(animal.animalKindId == 12 && inj[3] != '6', "Inj")
        viewState.showValidateError(animal.animalKindId == 14 && inj[3] != '6', "Inj")
        viewState.showValidateError(animal.animalKindId == 15 && inj[3] != '6', "Inj")
        viewState.showValidateError(animal.animalKindId == 16 && inj[3] != '6', "Inj")
        viewState.showValidateError(animal.animalKindId == 17 && inj[3] != '6', "Inj")
        viewState.showValidateError(inj.size != 12 && animal.identId == 1, "Inj")
        viewState.showValidateError(inj.size != 13 && animal.identId == 10, "Inj")

        viewState.showValidateError(
            ::animalMastCache.isInitialized && animal.mastId == 0 && !animalMastCache.isNullOrEmpty(),
            "Mast"
        )
        viewState.showValidateError(
            ::directionCache.isInitialized && animal.directionId == 0 && !directionCache.isNullOrEmpty(),
            "Direction"
        )

        viewState.showValidateError(animal.birthDate.isEmpty(), "BirthDate")
        viewState.showValidateError(animal.animalKindId == 0, "AnimalKindS")
        viewState.showValidateError(animal.identId == 0, "Identification")
        viewState.showValidateError(animal.katoId == 0, "Kato")
        viewState.showValidateError(animal.countryId == 0, "Country")
        viewState.showValidateError(animal.causeId == 0, "CauseRegistry")
    }

    fun onUploadFileClicked(uri: Uri?, context: Context?, activity_p: Activity?) {
        if (context != null) {
            regAnimalInt.setContext(context,activity_p!!)
        }
        launch {
            val result: Response<Int>? = regAnimalInt.saveSelectedFile(uri)
            var list:ArrayList<Int> = ArrayList()
            result?.body()?.let { list.add(it) }
            currentAnimal.animalBook?.files = list;
        }
        //animalBook.files = listOf(File(uri.path!!))
    }


    fun onPermissionDenied(string: String) {
        viewState.showMessage(string)
    }

    fun onCauseRegistryChanged(items: List<BaseFormat>, position: Int) {
        val list = ArrayList<Country>()

        when {
            items[position].code.equals("Reg_Offspring") -> {

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
            items[position].code.equals("Req_AcquisitionImport") -> {
                launch {
                    val result = referencesInt.getCountries()
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

    fun onKindChanged(code: String?) {
        launch {

            val list = ArrayList<Identity>()
            var result = referencesInt.getTypeIdentifications()

            when {

                code.equals("SmallCattle") -> {
                    viewState.showMrsSpinner(true)
                    val newIdentity = result.firstOrNull { it.code == "BR" }
                    val chIdentity = result.firstOrNull { it.code == "CH" }

                    minDay = -365
                    maxDay = 8
                    isTV = false

                    if (newIdentity != null)
                        list.add(newIdentity)
                    if (chIdentity != null)
                        list.add(chIdentity)
                    result = list
                    BaseFormat.toFormat(result)
                        ?.let { viewState.showTypeIdentification(it) }
                }

                code.equals("Pigs") -> {

                    minDay = -365
                    maxDay = 8
                    isTV = false

                    val newIdentity = result.firstOrNull { it.code == "TV" }
                    val chIdentity1 = result.firstOrNull { it.code == "CH" }
                    val chIdentity2 = result.firstOrNull { it.code == "TT" }
                    val chIdentity3 = result.firstOrNull { it.code == "TTU" }
                    val chIdentity4 = result.firstOrNull { it.code == "TTG" }

                    if (newIdentity != null)
                        list.add(newIdentity)
                    if (chIdentity1 != null)
                        list.add(chIdentity1)
                    if (chIdentity2 != null)
                        list.add(chIdentity2)
                    if (chIdentity3 != null)
                        list.add(chIdentity3)
                    if (chIdentity4 != null)
                        list.add(chIdentity4)
                    result = list
                    BaseFormat.toFormat(result)
                        ?.let { viewState.showTypeIdentification(it) }

                }

                code.equals("Cattle") || code.equals("SmallCattle")
                        || code.equals("Goats") || code.equals("Sheeps") || code.equals("Camels") -> {

                    minDay = -93
                    maxDay = 8
                    isTV = false

                    val newIdentity = result.firstOrNull { it.code == "BR" }
                    val chIdentity = result.firstOrNull { it.code == "CH" }

                    if (newIdentity != null)
                        list.add(newIdentity)
                    if (chIdentity != null)
                        list.add(chIdentity)
                    result = list
                    BaseFormat.toFormat(result)
                        ?.let { viewState.showTypeIdentification(it) }
                }

                else -> {
                    isTV = true
                    val newIden1 = result.firstOrNull { it.code == "TV" }
                    val newIden2 = result.firstOrNull { it.code == "CH" }
                    if (newIden1 != null)
                        list.add(newIden1)
                    if (newIden2 != null)
                        list.add(newIden2)
                    result = list
                    BaseFormat.toFormat(result)
                        ?.let { viewState.showTypeIdentification(it) }
                    viewState.showMrsSpinner(false)
                }
            }

        }

    }

    fun onIdentityChanged(code: String) {
        if (code == "TV") {
            if (isTV && code == "TV") {
                isChip = true
            } else {
                minDay = -365
                maxDay = 8
            }
        }
        if (isChip && isTV) {
            minDay = -365
            maxDay = 20
        }
        if (code == "CH" && isTV) {
            minDay = -365
            maxDay = 8
        }
    }
}