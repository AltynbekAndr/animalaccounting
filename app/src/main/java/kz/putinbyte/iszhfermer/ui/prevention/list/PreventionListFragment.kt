package kz.putinbyte.iszhfermer.ui.prevention.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_prevention_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.AnimalPreventiveActionModelItem
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.prevention.list.PreventionListPresenter
import kz.putinbyte.iszhfermer.presentation.prevention.list.PreventionListView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.prevention.list.rv.PreventionListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class PreventionListFragment : BaseFragment(), PreventionListView {

    override val layoutResId = R.layout.fragment_prevention_list

    companion object {
        const val CURRENT_KEY = "animalId"
        var animalID: Int? = null

        fun newInstance() = PreventionListFragment()

        fun newInstance(animalId: Int) = PreventionListFragment().apply {
            arguments = Bundle().apply {
                putInt(CURRENT_KEY, animalId)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: PreventionListPresenter

    @ProvidePresenter
    fun providePresenter(): PreventionListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(PreventionListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
                animalID = animalId
            }
    }

    lateinit var adapter: PreventionListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_prevention_list))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {
        preventionListCreateButton.setOnClickListener {
            presenter.onAddClicked(animalID!!)
        }
    }

    private fun initAdapter() {

        adapter = PreventionListAdapter { items, position ->
            presenter.onItemClicked(items, position)
        }

        preventionListRec.adapter = adapter
        preventionListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun setList(list: List<AnimalPreventiveActionModelItem>?) {
        adapter.items = list
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                preventionListFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

}