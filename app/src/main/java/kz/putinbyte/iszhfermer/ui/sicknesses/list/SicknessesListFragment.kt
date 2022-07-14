package kz.putinbyte.iszhfermer.ui.sicknesses.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kz.putinbyte.iszhfermer.ui.sicknesses.list.rv.SicknessListAdapter
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_sicknesses_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.AnimalSicknessModelItem
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.sicknesses.list.SicknessesListPresenter
import kz.putinbyte.iszhfermer.presentation.sicknesses.list.SicknessesListView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class SicknessesListFragment : BaseFragment(), SicknessesListView {

    override val layoutResId = R.layout.fragment_sicknesses_list

    companion object {

        var animalID: Int? = null

        fun newInstance() = SicknessesListFragment()

        const val CURRENT_KEY = "animalId"

        fun newInstance(animalId: Int?) = SicknessesListFragment().apply {
            arguments = Bundle().apply {
                animalId?.let {
                    putInt(CURRENT_KEY, it)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: SicknessesListPresenter

    @ProvidePresenter
    fun providePresenter(): SicknessesListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(SicknessesListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
            }
    }

    lateinit var adapter: SicknessListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_sicknesses_list))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {
        operatorListButton.setOnClickListener {
            presenter.onAddClicked()
        }
    }

    private fun initAdapter() {

        adapter = SicknessListAdapter { items, position ->
            presenter.onItemClicked(items, position)
        }
        operatorListRec.adapter = adapter
        operatorListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun setList(list: List<AnimalSicknessModelItem>?) {
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
                sicknessListFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }
}