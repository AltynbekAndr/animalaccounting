package kz.putinbyte.iszhfermer.ui.diagnostic.canceled

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_canceled_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.success.SuccessList
import kz.putinbyte.iszhfermer.presentation.diagnostic.canceled.CanceledListPresenter
import kz.putinbyte.iszhfermer.presentation.diagnostic.canceled.CanceledView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.diagnostic.canceled.rv.CanceledListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class CanceledListFragment(var animalID: Int? = null) : BaseFragment(), CanceledView {

    override val layoutResId = R.layout.fragment_canceled_list

    companion object {

        fun newInstance() = CanceledListFragment()
    }

    @InjectPresenter
    lateinit var listPresenter: CanceledListPresenter

    @ProvidePresenter
    fun providePresenter(): CanceledListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(CanceledListPresenter::class.java).apply {
                animalId = animalID
            }
    }

    lateinit var adapter: CanceledListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {

        adapter = CanceledListAdapter() { _, _ -> }
        canceledListRec.adapter = adapter
        canceledListRec.layoutManager =
            LinearLayoutManager(context)
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
                canceledFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

    override fun setList(list: List<SuccessList>) {
        adapter.items = list
    }

}