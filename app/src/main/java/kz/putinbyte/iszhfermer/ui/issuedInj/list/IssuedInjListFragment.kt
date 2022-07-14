package kz.putinbyte.iszhfermer.ui.issuedInj.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kz.putinbyte.iszhfermer.ui.issuedInj.list.rv.IssuedInjListListAdapter
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_issued_ing_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInjList
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.issuedInj.list.IssuedInjListPresenter
import kz.putinbyte.iszhfermer.presentation.issuedInj.list.IssuedInjListView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class IssuedInjListFragment : BaseFragment(), IssuedInjListView {

    override val layoutResId = R.layout.fragment_issued_ing_list

    companion object {

        var animalID: Int? = null

        private const val CURRENT_KEY = "animalId"

        fun newInstance() = IssuedInjListFragment()

        fun newInstance(animalId: Int?) = IssuedInjListFragment().apply {
            arguments = Bundle().apply {
                animalId?.let {
                    putInt(CURRENT_KEY, it)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: IssuedInjListPresenter

    @ProvidePresenter
    fun providePresenter(): IssuedInjListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(IssuedInjListPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_KEY)
                animalID = animalId
            }
    }

    lateinit var adapter: IssuedInjListListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_issuedInj_list))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {
        listCreateButton.setOnClickListener {
            presenter.onAddClicked(animalID!!)
        }
    }

    private fun initAdapter() {


        adapter = IssuedInjListListAdapter { _, _ -> }

        issuedInjListRec.adapter = adapter
        issuedInjListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun setList(list: List<ReplaceInjList>?) {
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
                issuedInjListFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

}