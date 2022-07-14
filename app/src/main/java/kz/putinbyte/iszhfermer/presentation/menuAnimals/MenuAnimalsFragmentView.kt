package kz.putinbyte.iszhfermer.presentation.menuAnimals

import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MenuAnimalsFragmentView : BaseView {

    @AddToEndSingle
    fun onChangeInternetConnect(isConnected: Boolean)

}