package kz.putinbyte.iszhfermer.presentation.add.owners

import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.animals.OwnersByKato
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.animals.owners.OwnersResponse
import kz.putinbyte.iszhfermer.model.interactors.CommonDataInteractor
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import timber.log.Timber
import javax.inject.Inject

class OwnersPresenter @Inject constructor(
    private val referencesInteractor: ReferencesInteractor,
    private val regAnimalInt: RegAnimalInteractor,
    private val commonDataInteractor: CommonDataInteractor
) : BasePresenter<OwnersView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadOwners()
    }

    fun loadOwners() {
        launch {
            try {
                val result  = referencesInteractor.getOwnersByKato(kato = regAnimalInt.katoId!!)
                viewState.setList(stringToOwners(result?.lists!!))
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }

    }

    private fun stringToOwners(lists: List<OwnersByKato>): List<Owners> =
        lists.map {
            Owners(id = it.ownerId, fullName = it.fullNameRu)
        }

    fun searchOwners(fio: String? = null, inn:Long? = null){

        launch {
            try {
                val owner = OwnersResponse(1, 20, true, "")
                val result = referencesInteractor.getAlOwners(fio, inn, owner)
                handleResult(result,{
                    viewState.setList(it.data.list!!)
                }){}

            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e)
            }
        }
    }

    fun onItemClicked(id: Int) {

    }

}
