package kz.putinbyte.iszhfermer.ui.research.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kz.putinbyte.iszhfermer.ui.research.list.rv.ResearchListAdapter
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_research_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.presentation.research.list.ResearchListPresenter
import kz.putinbyte.iszhfermer.presentation.research.list.ResearchListView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class ResearchListFragment : BaseFragment(), ResearchListView {

    override val layoutResId = R.layout.fragment_research_list

    companion object {

        var animalID:Int? = null

        fun newInstance() = ResearchListFragment()

        const val CURRENT_KEY = "animalId"
        fun newInstance(animalId:Int?) = ResearchListFragment().apply {
            arguments = Bundle().apply {
                if (animalId != null) {
                    putInt(CURRENT_KEY,animalId)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: ResearchListPresenter

    @ProvidePresenter
    fun providePresenter(): ResearchListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(ResearchListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
                animalID = animalId
            }
    }

    lateinit var adapter: ResearchListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_research_list))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {

        researchCreateButton.setOnClickListener {
            presenter.onButtonClicked(arrayListOf(animalID!!))
        }

    }

    private fun initAdapter() {

        adapter = ResearchListAdapter { items, position ->
            presenter.onItemClicked(items, position)
        }
        contListRec.adapter = adapter
        contListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun setList(list: List<AnimalResearchModelItem>) {
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
                researchListFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }
}