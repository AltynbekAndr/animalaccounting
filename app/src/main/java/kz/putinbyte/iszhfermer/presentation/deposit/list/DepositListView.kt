package kz.putinbyte.iszhfermer.presentation.deposit.list

import kz.putinbyte.iszhfermer.entities.animals.deposit.DepositList
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface DepositListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<DepositList>)

    @Skip
    fun showLoader(show:Boolean)
}