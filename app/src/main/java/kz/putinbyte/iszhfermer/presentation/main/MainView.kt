package kz.putinbyte.iszhfermer.presentation.main

import kz.putinbyte.iszhfermer.entities.animals.AnimalList
import kz.putinbyte.iszhfermer.entities.requests.UserInfoList
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface MainView : BaseView {

    @AddToEndSingle
    fun setTitle(title: String)

    @Skip
    fun showConfirmExitDialog()

    @Skip
    fun showAlert(list: List<AnimalList.Animals>?)

    @Skip
    fun showUserAlert(userInfo: UserInfoList.UserInfo?)
}