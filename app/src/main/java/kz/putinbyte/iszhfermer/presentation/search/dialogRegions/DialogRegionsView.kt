package kz.putinbyte.iszhfermer.presentation.search.dialogRegions

import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface DialogRegionsView: BaseView {

    @AddToEndSingle
    fun setList(region: Region)

    @Skip
    fun showLoader(show:Boolean)

}
