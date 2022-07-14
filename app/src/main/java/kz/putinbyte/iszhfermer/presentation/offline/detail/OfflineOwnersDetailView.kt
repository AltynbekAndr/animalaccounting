package kz.putinbyte.iszhfermer.presentation.offline.detail

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import moxy.viewstate.strategy.alias.AddToEndSingle
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import moxy.viewstate.strategy.alias.Skip

interface OfflineOwnersDetailView : BaseView {

    @AddToEndSingle
    fun showAnimalKind(items: List<BaseFormat>)

    @AddToEndSingle
    fun visibleReset(visible: Boolean)

    @AddToEndSingle
    fun resetError()

    @AddToEndSingle
    fun showValidateError(error: Boolean, idName: String)

    @Skip
    fun showLoader(show: Boolean)

    @AddToEndSingle
    fun showData(owners: Owners)

}