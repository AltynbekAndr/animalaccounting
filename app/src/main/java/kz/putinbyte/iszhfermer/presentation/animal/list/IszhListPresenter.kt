package kz.putinbyte.iszhfermer.presentation.animal.list

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.AnimalMastItem
import kz.putinbyte.iszhfermer.entities.animals.Gender
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class IszhListPresenter @Inject constructor(
        private val router: Router,
        private val referencesInteractor: ReferencesInteractor,
        private val commonDataInt: CommonDataInteractor,
        private val rm: ResourceManager
) : BasePresenter<IszhListView>() {

    var katoId: Int? = null
    var ownerId: Int? = null

    private lateinit var kindOfAnimalCache: List<KindOfAnimal>
    private lateinit var animalAllMastCache: List<AnimalMastItem>
    private lateinit var gendersCache: List<Gender>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    override fun attachView(view: IszhListView?) {
        super.attachView(view)
    }

    fun updateUI() {
        if (::kindOfAnimalCache.isInitialized)
            BaseFormat.toFormat(kindOfAnimalCache)?.let { viewState.showAnimalKindType(it) }
    }

    fun loadAnimalList(animalKind: String) {
        launch {
            if (::kindOfAnimalCache.isInitialized && ::gendersCache.isInitialized && ::animalAllMastCache.isInitialized) {
                viewState.showProgressBar(true)
                val result = referencesInteractor.getAnimalsByOwner(katoId!!, ownerId!!, animalKind)
                handleResult(result, { animals ->
                    viewState.showProgressBar(false)
                    val items = animals.data.listAnimals.map { animal ->
                        val animalKindName = kindOfAnimalCache.firstOrNull { it.id == animal.animalKindId }?.nameRu
                        animal.copy(
                                mast = animalAllMastCache.firstOrNull { it.id == animal.mastId }?.nameRu,
                                gender = gendersCache.firstOrNull { it.id == animal.genderId }?.nameRu,
                                animalKind = if (animalKindName == "Крупный рогатый скот") "КРС" else animalKindName
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
            kindOfAnimalCache = referencesInteractor.getKindsOfAnimals()
            animalAllMastCache = referencesInteractor.getAnimalAllMast()
            gendersCache = commonDataInt.gender
            updateUI()
            viewState.showProgressBar(false)
        }
    }

    fun animalRegister() {
        router.navigateTo(Screens.RegisterAnimal(katoId = katoId, ownerId = ownerId))
    }

    fun onItemClicked(animalId: Int) {
        router.navigateTo(Screens.Detail(iszhId = animalId))
    }

    fun onDeregisterClicked(ids: ArrayList<Int>?,inj:ArrayList<String>) {
        router.navigateTo(Screens.Deregister(ids,inj))
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