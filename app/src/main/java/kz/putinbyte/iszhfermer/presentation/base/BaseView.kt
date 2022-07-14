package kz.putinbyte.iszhfermer.presentation.base

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface BaseView : MvpView {

    @AddToEndSingle
    fun showMessage(msg: String)
}