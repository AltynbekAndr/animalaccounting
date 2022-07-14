package kz.putinbyte.iszhfermer.ui.fattening.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_fattening_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.fattening.FatteningList
import kz.putinbyte.iszhfermer.presentation.fattening.list.FatteningListPresenter
import kz.putinbyte.iszhfermer.presentation.fattening.list.FatteningListView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.fattening.list.rv.FatteningListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class FatteningListFragment : BaseFragment(), FatteningListView {

    override val layoutResId = R.layout.fragment_fattening_list

    companion object {

        fun newInstance() = FatteningListFragment()

        const val CURRENT_KEY = "animalId"
        fun newInstance(animalId: Int?) = FatteningListFragment().apply {
            arguments = Bundle().apply {
                if (animalId != null) {
                    putInt(CURRENT_KEY, animalId)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: FatteningListPresenter

    @ProvidePresenter
    fun providePresenter(): FatteningListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(FatteningListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
            }
    }

    lateinit var adapter: FatteningListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_fattening_list))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {
        listFatteningButton.setOnClickListener {
            presenter.onButtonClicked()
        }
    }

    private fun initAdapter() {

        adapter = FatteningListAdapter { _, _, _ ->
        }

        fatteningListRec.adapter = adapter
        fatteningListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun setList(list: List<FatteningList>) {
        adapter.items = list
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                fatteningList,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }
}