package kz.putinbyte.iszhfermer.presentation.region

import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.model.interactors.RegAnimalInteractor
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class RegionListPresenter @Inject constructor(
    private val router: Router,
    private val iszhInteractor: RegAnimalInteractor
) : BasePresenter<RegionView>() {

    var kato: Int? = null
    var code:Int? = null

    override fun attachView(view: RegionView?) {
        super.attachView(view)
    }

    fun onOwnersClicked(id:Int,katoId:Int) {
        router.navigateTo(Screens.Owners(id))
    }

    fun onAddClicked(katoId:Int) {
        router.navigateTo(Screens.RegisterAnimal(katoId = katoId))
    }

}