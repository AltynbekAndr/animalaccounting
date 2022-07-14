package kz.putinbyte.iszhfermer.ui.owners

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_owners.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.OwnersByKato
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.ownersList.OwnersListPresenter
import kz.putinbyte.iszhfermer.presentation.ownersList.OwnersListView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.owners.rv.OwnersAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class OwnersList : BaseFragment(), OwnersListView {

    override val layoutResId = R.layout.fragment_owners

    companion object {

        fun newInstance() = OwnersList()

        private const val CURRENT_KATO_ID_KEY = "KATO"

        fun newInstance(katoId: Int) = OwnersList().apply {
            arguments = Bundle().apply {
                putInt(CURRENT_KATO_ID_KEY, katoId)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: OwnersListPresenter
    lateinit var adapter: OwnersAdapter
    var kato: Int? = null

    @ProvidePresenter
    fun providePresenter(): OwnersListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(OwnersListPresenter::class.java).apply {
                katoId = arguments?.getInt(CURRENT_KATO_ID_KEY)
                kato = katoId
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_owners))

        initAdapter()
        initListeners()
    }

    private fun initAdapter() {
        adapter = OwnersAdapter { items, position ->
            presenter.onAnimalListClicked(kato!!, items!![position].ownerId!!)
        }
        owners_rv.adapter = adapter
        owners_rv.layoutManager = LinearLayoutManager(context)
    }

    private fun initListeners() {
        listOwnersButton.setOnClickListener {
            presenter.onOwnersClicked()
        }
    }

    override fun setList(list: List<OwnersByKato>) {
        adapter.items = list
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
                ownersListFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }
}