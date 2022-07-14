package kz.putinbyte.iszhfermer.presentation.issuedInj.list

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import kz.putinbyte.iszhfermer.utils.MyUtils
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class IssuedInjListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInt: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<IssuedInjListView>() {

    var animalId: Int? = null

    override fun attachView(view: IssuedInjListView?) {
        super.attachView(view)
        loadInjList()
    }

    private fun loadInjList() {
        viewState.showLoader(true)
        launch {
            animalId?.let { id ->
                val result = referencesInt.getReplaceInjsById(id)
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()) {
                        res.data.forEach { each ->
                            each.dateIssue = MyUtils.toMyDate(each.dateIssue)
                            launch {
                                val ownerName = referencesInt.getOwnersById(each.userId)
                                handleResult(ownerName,{
                                    val lastName = it.data.lastName ?: ""
                                    val firstName = it.data.firstName ?: ""
                                    val middleName = it.data.middleName ?: ""
                                    each.note = each.note ?: ""
                                    each.user = "$lastName $firstName $middleName"
                                    viewState.showLoader(false)
                                    viewState.setList(res.data)
                                }){

                                }

                            }
                        }
                    }else{
                        viewState.showLoader(false)
                        viewState.showMessage(rm.getString(R.string.noData))
                    }
                    viewState.showLoader(false)
                }) {
                    handleError2(it, rm) { msg ->
                        viewState.showLoader(false)
                        viewState.showMessage(msg)
                    }
                }
            }
        }
    }

    fun onAddClicked(animalId: Int) {
        router.navigateTo(Screens.IssuedInj(animalId))
    }
}