package kz.putinbyte.iszhfermer.presentation.menuAnimals

import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MenuAnimalsFragmentPresenter @Inject constructor(
    private val router: Router
) : BasePresenter<MenuAnimalsFragmentView>() {

    var isConnect = true

    override fun attachView(view: MenuAnimalsFragmentView?) {
        super.attachView(view)
    }

    fun updateInternetStatus(isConnect: Boolean){
        this.isConnect = isConnect
        viewState.onChangeInternetConnect(this.isConnect)
    }

    fun onAnimalsClicked() {
        if (isConnect){
            router.navigateTo(Screens.Regions())
        }else{
            router.navigateTo(Screens.OfflineAnimalsList)
        }
    }

    fun onSearchButtonClicked() {
        router.navigateTo(Screens.Search(null,""))
    }

    fun onRvlClicked() {
        router.navigateTo(Screens.Rvl)
    }

    fun onOfflineOwnersClicked() {
        router.navigateTo(Screens.OfflineOwners)
    }
}