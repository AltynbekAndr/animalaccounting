package kz.putinbyte.iszhfermer.presentation.animal.list

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.Gender
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.collections.ArrayList

class OfflineListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val regAnimalInt: RegAnimalInteractor,
    private val commonDataInt: CommonDataInteractor,
    private val rm: ResourceManager
) : BasePresenter<OfflineListView>() {


    private lateinit var kindOfAnimalCache: List<KindOfAnimal>
    private lateinit var gendersCache: List<Gender>
    private lateinit var countryCache: List<Country>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    override fun attachView(view: OfflineListView?) {
        super.attachView(view)
    }

    fun updateUI() {
        if (::kindOfAnimalCache.isInitialized)
            BaseFormat.toFormat(kindOfAnimalCache)?.let { viewState.showAnimalKindType(it) }
    }

    fun loadAnimalList(animalKindId: Int?) {
        launch {
            if (::kindOfAnimalCache.isInitialized && ::gendersCache.isInitialized && ::countryCache.isInitialized) {
                viewState.showProgressBar(true)
                val result = regAnimalInt.getOfflineRegisteredAnimals()
                handleResult(result, { animals ->
                    viewState.showProgressBar(false)
                    val items = animals.data.listAnimals.filter { if (animalKindId != 0) it.animalKindId == animalKindId else true }.map { animal ->
                        val animalKindName = kindOfAnimalCache.firstOrNull { it.id == animal.animalKindId }?.nameRu
                        val sdf = SimpleDateFormat("yyyy-mm-dd")
                        animal.copy(
                            gender = gendersCache.firstOrNull { it.id == animal.genderId }?.nameRu,
                            animalKind = if (animalKindName == "Крупный рогатый скот") "КРС" else animalKindName,
                            country = countryCache.firstOrNull { it.id == animal.countryId }?.nameRu,
                            age = animal.birthDate
                        )
                    }
                    viewState.setList(items)
                }) {
                    handleError2(it, rm) { msg ->
                        viewState.showProgressBar(false)
                        viewState.showMessage(msg)
                    }
                }

            } else {
                refreshLocalData()
            }
        }
    }

    private fun refreshLocalData() {
        viewState.showProgressBar(true)
        launch {
            kindOfAnimalCache = referencesInt.getKindsOfAnimals()
            gendersCache = commonDataInt.gender
            countryCache = referencesInt.getCountries()
            updateUI()
            viewState.showProgressBar(false)
        }
    }

    fun animalRegister() {
        router.navigateTo(Screens.RegisterAnimal())
    }

    fun onItemClicked(animalId: Int) {
        router.navigateTo(Screens.Detail(iszhId = animalId))
    }

    fun onDeregisterClicked(ids: ArrayList<Int>?,injs: ArrayList<String>?) {
        router.navigateTo(Screens.Deregister(ids,injs))
    }

    fun setGroupSickness(ids: ArrayList<Int>?) {
        router.navigateTo(Screens.Sicknesses(ids))
    }

    fun setGroupResearch(ids: ArrayList<Int>) {
        router.navigateTo(Screens.Research(ids))
    }

    fun setGroupPrevention(ids: ArrayList<Int>) {
        router.navigateTo(Screens.Prevention(ids))
    }

}