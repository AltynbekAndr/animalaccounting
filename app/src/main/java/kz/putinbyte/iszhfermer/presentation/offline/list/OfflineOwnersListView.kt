package kz.putinbyte.iszhfermer.presentation.offline.list

import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface OfflineOwnersListView : BaseView {

    @AddToEndSingle
    fun setList(list: List<Owners>?)

    @Skip
    fun showLoader(show:Boolean)

    @Skip
    fun showOwnersDialog()

    @Skip
    fun showRemoveDialog(items: List<Owners>?, position: Int)
}