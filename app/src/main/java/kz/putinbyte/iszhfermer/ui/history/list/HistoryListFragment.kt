package kz.putinbyte.iszhfermer.ui.history.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_history_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.history.HistoryList
import kz.putinbyte.iszhfermer.presentation.history.list.HistoryListPresenter
import kz.putinbyte.iszhfermer.presentation.history.list.HistoryListView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.history.list.rv.HistoryListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class HistoryListFragment : BaseFragment(), HistoryListView {

    override val layoutResId = R.layout.fragment_history_list

    companion object {

        fun newInstance() = HistoryListFragment()

        const val CURRENT_HISTORY_KEY = "animalId"

        fun newInstance(animalId: Int?) = HistoryListFragment().apply {
            arguments = Bundle().apply {
                if (animalId != null) {
                    putInt(CURRENT_HISTORY_KEY, animalId)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: HistoryListPresenter

    @ProvidePresenter
    fun providePresenter(): HistoryListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(HistoryListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_HISTORY_KEY)
            }
    }

    lateinit var adapter: HistoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_history_list))
        initAdapter()
    }

    private fun initAdapter() {

        adapter = HistoryListAdapter { _, _ -> }
        historyListRec.adapter = adapter
        historyListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun setList(list: List<HistoryList>) {
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
                historyFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }
}