package kz.putinbyte.iszhfermer.ui.search.dialogRegions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import iszhfermer.R
import kotlinx.android.synthetic.main.custom_regions_dialog.*
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.SearchListRegion
import kz.putinbyte.iszhfermer.presentation.search.dialogRegions.DialogRegionsListPresenter
import kz.putinbyte.iszhfermer.presentation.search.dialogRegions.DialogRegionsView
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.search.rv.DialogRegionsListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

class RegionsBottomSheetFragment(private val listener: Listener) : BaseBottomSheetFragment(),
    DialogRegionsView {

    override val layoutRes = R.layout.custom_regions_dialog

    val TAG = javaClass.simpleName

    @InjectPresenter
    lateinit var presenter: DialogRegionsListPresenter
    lateinit var adapter: DialogRegionsListAdapter
    private var region: Region? = null

    @ProvidePresenter
    fun providePresenter(): DialogRegionsListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(DialogRegionsListPresenter::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initListeners()
    }

    private fun initAdapter() {

        adapter = DialogRegionsListAdapter { items, position, buttonType ->
            val clickedItem = items[position]
            when (buttonType) {
                SearchListRegion.NEXT -> {
                    presenter.setNextRegionList(clickedItem.code.toLong())
                }
                SearchListRegion.CHOOSE -> {
                    listener.setOnClick(items[position])
                    dismiss()
                }
            }
        }

        regionsRec.adapter = adapter
        regionsRec.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun setList(region: Region) {
        this.region = region
        adapter.items = region.animalAmountByKatos
        regionsBackButton.visible(region.katos.size != 1)
    }

    private fun initListeners() {
        regionsBackButton.setOnClickListener {
            try {
                val katos = this.region?.katos ?: listOf()
                val parentKato = if (katos.size <= 2)
                    katos.last()
                else
                    katos.elementAt(katos.size - 3)
                presenter.setNextRegionList(parentKato.code.toLong())
            } catch (e: Exception) {
                regionsBackButton.visible(false)
                LogUtils.error(TAG, e.message)
            }

        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    override fun showLoader(show: Boolean) {
        region_progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    interface Listener {
        fun setOnClick(items: Region.AnimalAmountByKato)
    }
}