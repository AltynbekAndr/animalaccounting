package kz.putinbyte.iszhfermer.presentation.add.owners

import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface OwnersView: BaseView {

    @AddToEndSingle
    fun setList(list: List<Owners>)
}
