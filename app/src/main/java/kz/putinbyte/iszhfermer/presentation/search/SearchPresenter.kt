package kz.putinbyte.iszhfermer.presentation.search

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.MultiSearchSearch
import kz.putinbyte.iszhfermer.entities.animals.search.SearchResponse
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class SearchPresenter @Inject constructor(
        val referencesInteractor: ReferencesInteractor,
        val regAnimalInteractor: RegAnimalInteractor,
        val router: Router
) : BasePresenter<SearchView>() {

    var animalKindId: Int? = null
    var status: Int? = null
    var inj: String? = null
    var ownerId: Int? = null
    var katoId: Int? = null
    private lateinit var kindOfAnimalCache: List<KindOfAnimal>
    lateinit var multiSearch: MultiSearchSearch

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshLocalData()
    }

    private fun updateUI() {
        if (::kindOfAnimalCache.isInitialized) BaseFormat.toFormat(kindOfAnimalCache)
                ?.let {
                    viewState.showKindOfAnimal(BaseFormat.defaultParam + it)
                }
    }

    private fun refreshLocalData() {
        viewState.showLoader(true)
        launch {
            kindOfAnimalCache = referencesInteractor.getKindsOfAnimals()
            val list = ArrayList<BaseFormat>()
            list.add(BaseFormat(1, null, "Зарегистрирован", ""))
            list.add(BaseFormat(2, null, "Снять с учета", ""))
            list.add(BaseFormat(3, null, "Неопределен", ""))
            list.add(BaseFormat(4, null, "Передан в СПК/аренду", ""))
            viewState.showStatus(BaseFormat.defaultParam + list)
            updateUI()
            viewState.showLoader(false)
        }
    }

    override fun attachView(view: SearchView?) {
        super.attachView(view)
        updateUI()
    }

    fun loadFindAnimalList(pageIndex:Int) {
        viewState.showLoader(true)
        launch {
            try {
                multiSearch = MultiSearchSearch(animalKindId, inj, ownerId, katoId, status)
                val result = referencesInteractor.getFindAnimals(multiSearch, pageIndex)

                if (!result?.lists.isNullOrEmpty()) {
                    val allMast = referencesInteractor.getAnimalAllMast()
                    val allAnimalKind = referencesInteractor.getKindsOfAnimals()

                    result?.lists?.forEach { animals ->
                        animals.mastString = allMast.first { it.id == animals.mastId }.nameRu
                        animals.animalKindString = allAnimalKind.first { it.id == animals.animalKindId }.nameRu
                        val resultOwners = referencesInteractor.getOwnersById(animals.ownerId)
                        handleResult(resultOwners,{
                            animals.ownerFullName = if (it != null) {
                                it.data.lastName + " " + it.data.firstName + " " + it.data.middleName
                            } else "Неизвестно"
                        }){}
                        animals.genderString = if (animals.genderId == 1) "Самец" else "Самка"

                        animals.animalKindString =
                                if (animals.animalKindString.equals("Крупный рогатый скот")) "КРС" else animals.animalKindString


                    }
                } else {
                    viewState.showLoader(false)
                    viewState.showEmptyMessage()
                }
                viewState.setList(result?.lists as ArrayList<SearchResponse.Lists>, pageIndex == 0)
                viewState.showLoader(false)
            } catch (e: Exception) {
                Timber.e(e)
                e.printStackTrace()
            }
        }
    }

    fun onAnimalKindChanged(animalKindID: Int?) {
        animalKindId = animalKindID
    }

    fun onStatusChanged(id: Int?) {
        status = id
    }

    fun onItemClicked(animalId: Int) {
        router.navigateTo(Screens.Detail(iszhId = animalId))
    }

    fun onKatoIdChanged(id: Int) {
        regAnimalInteractor.katoId = id
        katoId = id
    }

    fun onOwnerChanged(id: Int) {
        ownerId = id
    }
}