package kz.putinbyte.iszhfermer.presentation.searchListView

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.animals.AnimalsModel
import kz.putinbyte.iszhfermer.entities.animals.MultiSearch
import kz.putinbyte.iszhfermer.model.repository.RegAnimalRepository
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SearchListPresenter @Inject constructor(
    private val router: Router,
    private val iszhInteractor: RegAnimalRepository,
) : BasePresenter<SearchListView>() {

    fun gettingListAnimals(
        identifier: String? = null,
        inj: String? = null
    ) {
        launch {
            val model = AnimalsModel(MultiSearch(identifier = identifier, inj = inj))
            val result = iszhInteractor.listAnimals(null, null, model)
            handleResult(result, {
                viewState.getList(it.data)
            }) { error ->
                println()
            }
        }
    }

    enum class TypSearch{
        INJ,
        IDENTIFIER
    }
}