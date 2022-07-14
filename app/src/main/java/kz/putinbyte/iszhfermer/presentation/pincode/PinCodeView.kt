package kz.putinbyte.iszhfermer.presentation.pincode

import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface PinCodeView : BaseView {

    @AddToEndSingle
    fun showCheckDialog()

    @AddToEndSingle
    fun showCreateDialog()

    @AddToEndSingle
    fun checkPinCode(pinCode: String)

    @AddToEndSingle
    fun savePinCode()

}