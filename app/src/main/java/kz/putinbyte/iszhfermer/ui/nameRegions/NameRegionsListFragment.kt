package kz.putinbyte.iszhfermer.ui.nameRegions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kz.putinbyte.iszhfermer.ui.nameRegions.rv.NameRegionsListAdapter
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_name_regions.*
import kz.putinbyte.iszhfermer.component.CheckNetworkConnection
import kz.putinbyte.iszhfermer.component.NetworkAvailability
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.model.data.enums.ListRegion
import kz.putinbyte.iszhfermer.presentation.addresses.NameRegionsListPresenter
import kz.putinbyte.iszhfermer.presentation.addresses.NameRegionsListView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class NameRegionsListFragment : BaseFragment(), NameRegionsListView {

    override val layoutResId = R.layout.fragment_name_regions

    companion object {

        var katoId: Int? = null
        const val KATO_KEY = "katoId"
        fun newInstance(katoId: Int?) = NameRegionsListFragment().apply {
            arguments = Bundle().apply {
                katoId?.let { putInt(KATO_KEY, it) }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: NameRegionsListPresenter

    @ProvidePresenter
    fun providePresenter(): NameRegionsListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(NameRegionsListPresenter::class.java).apply {
                katoId = arguments?.getInt(KATO_KEY)
            }
    }

    lateinit var adapter: NameRegionsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_addresses))
        initAdapter()
    }

    override fun setList(region: List<Region.AnimalAmountByKato>) {
        adapter.items = region
    }

    override fun showProgressBar(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                nameRegionsFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    private fun initAdapter() {
        adapter = NameRegionsListAdapter() { items, position, buttonType ->
            val clickedItem = items?.get(position)
            if (clickedItem != null) {
                when (buttonType) {
                    ListRegion.NEXT_LEVEL -> {
                        presenter.setNextRegionList(clickedItem.code.toLong())
                    }
                    ListRegion.ADD_ANIMAL -> {
                        presenter.onAddClicked(clickedItem.id)
                    }
                    ListRegion.OWNER_BUTTON -> {
                        presenter.setOwnersList(clickedItem.id)
                    }
                    else -> {}
                }
            }
        }

        CheckNetworkConnection.get(requireContext()).observe(viewLifecycleOwner) {
            val isConnect: Boolean = it == NetworkAvailability.CONNECTED
            val disconnect: Boolean = it == NetworkAvailability.DISCONNECTED
            adapter.chekConnect(disconnect)
        }

        regionsRec.adapter = adapter
        regionsRec.layoutManager =
            LinearLayoutManager(context)
    }
}