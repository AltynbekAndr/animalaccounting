package kz.putinbyte.iszhfermer.presentation.scanner

import moxy.viewstate.strategy.alias.Skip
import kz.putinbyte.iszhfermer.presentation.base.BaseView

interface ScannerView : BaseView {

    @Skip
    fun back()

    @Skip
    fun showErrorMessage(message: String)
}