package kz.putinbyte.iszhfermer.presentation.deposit.list

import iszhfermer.R
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.ReferencesInteractor
import kz.putinbyte.iszhfermer.model.system.ResourceManager
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class DepositListPresenter @Inject constructor(
    private val router: Router,
    private val referencesInteractor: ReferencesInteractor,
    private val rm: ResourceManager
) : BasePresenter<DepositListView>() {

    var animalId: Int? = null

    override fun attachView(view: DepositListView?) {
        super.attachView(view)
        loadDeposit()
    }

    private fun loadDeposit(){
        viewState.showLoader(true)
        launch {
            animalId?.let {
                val result = referencesInteractor.getDepositById(it)
                handleResult(result, { res ->
                    if (res.data.isNotEmpty()) {
                        viewState.showLoader(false)
                        viewState.setList(res.data)
                    }else{
                        viewState.showLoader(false)
                        viewState.showMessage(rm.getString(R.string.noData))
                    }

                }) {
                    handleError2(it, rm) { msg ->
                        viewState.showLoader(false)
                        viewState.showMessage(msg)
                    }
                }
            }
        }
    }

    fun onButtonClicked() {
        animalId?.let {
            router.navigateTo(Screens.Deposit(it))
        }
    }
}