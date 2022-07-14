package kz.putinbyte.iszhfermer.presentation.mainPresenter

import kz.putinbyte.iszhfermer.Screens
import kz.putinbyte.iszhfermer.presentation.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainFragmentPresenter @Inject constructor(
    private val router: Router
) : BasePresenter<MainFragmentView>() {

    override fun attachView(view: MainFragmentView?) {
        super.attachView(view)
    }
    fun onAnimalsClicked() {
        router.navigateTo(Screens.MenuFragment)
    }

    fun textClick() {
        router.navigateTo(Screens.Individuals)
    }
}