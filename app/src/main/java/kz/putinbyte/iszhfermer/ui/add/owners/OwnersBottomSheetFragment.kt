package kz.putinbyte.iszhfermer.ui.add.owners

import android.os.Bundle
import android.view.View
import iszhfermer.R
import kotlinx.android.synthetic.main.custom_owners_dialog.*
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.ui.add.owners.rv.OwnersAdapter
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import androidx.recyclerview.widget.LinearLayoutManager
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.presentation.add.owners.OwnersPresenter
import kz.putinbyte.iszhfermer.presentation.add.owners.OwnersView

class OwnersBottomSheetFragment(var listener: Listener) : BaseBottomSheetFragment(),
    OwnersView {

    override val layoutRes = R.layout.custom_owners_dialog

    @InjectPresenter
    lateinit var presenter: OwnersPresenter
    lateinit var adapter: OwnersAdapter

    @ProvidePresenter
    fun providePresenter(): OwnersPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(OwnersPresenter::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initListeners()
    }

    private fun initAdapter() {

        adapter = OwnersAdapter { items, position ->
            listener.setOnClick(items[position])
            presenter.onItemClicked(items[position].id!!)
            dismiss()
        }

        ownersRecycler.adapter = adapter
        ownersRecycler.layoutManager = LinearLayoutManager(context)

    }

    private fun initListeners() {

        fioButtonSearch.setOnClickListener {
            try {
                presenter.searchOwners(fio = ownersFioEdit.text.toString())
            } catch (e: Exception) {
                showMessage(e.toString())
            }
        }

        innButtonSearch.setOnClickListener {
            try {
                presenter.searchOwners(inn = ownersIinEdit.text.toString().toLong())
            } catch (e: Exception) {
                showMessage(e.toString())
            }
        }


    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    override fun setList(list: List<Owners>) {
        adapter.items = list
    }

    interface Listener {
        fun setOnClick(items: Owners)
    }
}