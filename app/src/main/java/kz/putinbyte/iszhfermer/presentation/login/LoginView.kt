package kz.putinbyte.iszhfermer.presentation.login

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LoginView : BaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setLoginFieldsEmptyError()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setPasswordFieldEmptyError()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setEmailFieldEmptyError()
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setLoadingState(value: Boolean)

    @Skip
    fun showErrorDialog(ex: Throwable)

    @Skip
    fun setAuthData(username: String?, pass: String?)

    @Skip
    fun showErrorMessage(ex: Throwable)

    @Skip
    fun showErrorMessage(message: String)

    @AddToEndSingle
    fun showViews()

    @AddToEndSingle
    fun showPinDialog()
}