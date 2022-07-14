package kz.putinbyte.iszhfermer.ui.diagnostic.success

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_success_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.success.SuccessList
import kz.putinbyte.iszhfermer.presentation.diagnostic.success.SuccessPresenter
import kz.putinbyte.iszhfermer.presentation.diagnostic.success.SuccessView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.diagnostic.success.rv.SuccessListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class SuccessListFragment(var animalID: Int? = null) : BaseFragment(), SuccessView {

    override val layoutResId = R.layout.fragment_success_list

    companion object {

        fun newInstance() = SuccessListFragment()
    }

    @InjectPresenter
    lateinit var presenter: SuccessPresenter

    @ProvidePresenter
    fun providePresenter(): SuccessPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(SuccessPresenter::class.java).apply {
                animalId = animalID
            }
    }

    lateinit var adapter: SuccessListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        setListeners()

    }

    private fun setListeners() {

    }

    private fun initAdapters() {

        adapter = SuccessListAdapter(requireContext()) { _, _ -> }
        successListRec.adapter = adapter
        successListRec.layoutManager =
            LinearLayoutManager(context)

    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun setList(list: List<SuccessList>) {
        adapter.items = list
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                successFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

}