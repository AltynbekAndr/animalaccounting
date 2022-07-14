package kz.putinbyte.iszhfermer.ui.deposit.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_deposit_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.deposit.DepositList
import kz.putinbyte.iszhfermer.presentation.deposit.list.DepositListPresenter
import kz.putinbyte.iszhfermer.presentation.deposit.list.DepositListView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.deposit.list.rv.DepositListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class DepositListFragment : BaseFragment(), DepositListView {

    override val layoutResId = R.layout.fragment_deposit_list

    companion object {

        fun newInstance() = DepositListFragment()

        const val CURRENT_KEY = "animalId"

        fun newInstance(animalId: Int) = DepositListFragment().apply {
            arguments = Bundle().apply {
                putInt(CURRENT_KEY, animalId)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: DepositListPresenter

    @ProvidePresenter
    fun providePresenter(): DepositListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(DepositListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
            }
    }

    lateinit var adapter: DepositListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_deposit_list))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {
        listDepositButton.setOnClickListener {
            presenter.onButtonClicked()
        }
    }

    private fun initAdapter() {

        adapter = DepositListAdapter { _, _, _ ->
        }
        depositListRec.adapter = adapter
        depositListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun setList(list: List<DepositList>) {
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
                depositList,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }
}