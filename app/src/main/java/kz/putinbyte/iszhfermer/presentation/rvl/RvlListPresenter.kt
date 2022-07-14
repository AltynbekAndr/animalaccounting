package kz.putinbyte.iszhfermer.presentation.rvl

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.entities.animals.AnimalsModel
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.entities.animals.MultiSearch
import kz.putinbyte.iszhfermer.model.repository.RegAnimalRepository
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.ui.rvl.rv.TestRvl
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class RvlListPresenter @Inject constructor(
    private val router: Router,
    private val iszhInteractor: RegAnimalRepository,
    private val rm: ResourceManager
) : BasePresenter<RvlListView>() {
    private var regions = MutableLiveData(false)
    private var city = MutableLiveData(false)
    private var areas = MutableLiveData(false)
    private var animals = MutableLiveData(false)
    private var list = MutableLiveData(false)

    var animalId: Int? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getKato()
        getTypeAnimal()
    }

    override fun attachView(view: RvlListView?) {
        super.attachView(view)
    }

    fun onItemClicked(items: List<TestRvl>?, position: Int) {
    }

    fun onAddClicked(animalId: Int?, list: ArrayList<ListModel>) {
        router.navigateTo(Screens.CreateInventory(animalId, list))
    }

    fun getKato(id: Int = 0, reg: Roles = Roles.AREA) {
        launch {
            val result = iszhInteractor.getRegions(id)
            handleResult(result, {
                when (reg) {
                    Roles.REGION -> {
                        viewState.setKatoRegion(it.data)
                    }
                    Roles.CITE -> {
                        viewState.setKatoCite(it.data)
                    }
                    else -> {
                        areas.value = true
                        listBol()
                        viewState.setListKato(it.data)
                    }
                }
            }) { error ->
                handleError(error, rm)
                viewState.errorMessage()
            }
        }
    }

    private fun getTypeAnimal() {
        launch {
            val result = iszhInteractor.getTypeAnimal()
            handleResult(result, {
                animals.value = true
                listBol()
                viewState.setTypeAnimal(it.data)
            }) { error ->
                handleError(error, rm)
                viewState.errorMessage()
            }
        }
    }

    fun gettingListAnimals(
        id: Int? = null,
        animalLindCode: String? = null,
        pageIndex: Int? = 0,
        pageSize: Int? = 10,
        isSpk: Boolean? = null,
        isDead: Boolean? = null,
    ) {
        launch {
            val model: AnimalsModel =
                if (id == -1 && animalLindCode == "" || id == null && animalLindCode == null) {
                    AnimalsModel(null, pageIndex, pageSize)
                } else {
                    AnimalsModel(
                        MultiSearch(id, animalKindCode = animalLindCode),
                        pageIndex, pageSize
                    )
                }
            val result = iszhInteractor.listAnimals(isSpk, isDead, model)
            handleResult(result, {
                viewState.setGettingListAnimals(it.data)
                list.value = true
                listBol()
            }) { error ->
                handleError(error, rm)
                viewState.errorMessage()
            }
        }
    }

    private fun listBol() {
        viewState.setSuccessfulLoading(
            areas.value,
            animals.value,
            list.value)
    }

    enum class Roles {
        AREA,
        REGION,
        CITE
    }
}