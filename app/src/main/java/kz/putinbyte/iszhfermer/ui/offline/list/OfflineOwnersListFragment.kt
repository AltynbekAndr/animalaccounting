package kz.putinbyte.iszhfermer.ui.offline.list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_offline_owners_list.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.offline.list.OfflineOwnersListPresenter
import kz.putinbyte.iszhfermer.presentation.offline.list.OfflineOwnersListView
import kz.putinbyte.iszhfermer.ui.add.owners.OwnersBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.offline.list.rv.OfflineOwnersListAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class OfflineOwnersListFragment : BaseFragment(), OfflineOwnersListView {

    override val layoutResId = R.layout.fragment_offline_owners_list

    companion object {
        fun newInstance() = OfflineOwnersListFragment()
    }

    @InjectPresenter
    lateinit var presenter: OfflineOwnersListPresenter

    @ProvidePresenter
    fun providePresenter(): OfflineOwnersListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(OfflineOwnersListPresenter::class.java)
    }

    lateinit var adapter: OfflineOwnersListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_list_owners_offline))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {
        offlineOwnersListButton.setOnClickListener {
            offlineOwnersListButton.isClickable = false
            presenter.onUploadClicked()
        }
    }

    private fun initAdapter() {

        adapter = OfflineOwnersListAdapter(this::onItemClicked, this::onItemRemoveClicked)
        offlineOwnersListRec.adapter = adapter
        offlineOwnersListRec.layoutManager =
            LinearLayoutManager(context)
    }

    override fun showOwnersDialog() {
        val bottomSheetDialogFragment =
            OwnersBottomSheetFragment(object : OwnersBottomSheetFragment.Listener {
                override fun setOnClick(items: Owners) {
                    presenter.onOwnerChanged(items)
                }
            })

        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )

        Handler(Looper.getMainLooper()).postDelayed({
            offlineOwnersListButton.isClickable = true
        }, 500)
    }

    fun onItemClicked(items: List<Owners>?, position: Int) {
        items?.let {
            presenter.onOwnerChanged(it[position])
        }

    }

    private fun onItemRemoveClicked(items: List<Owners>?, position: Int) {
        presenter.showAlertRemoveDialog(items, position)
    }

    override fun showRemoveDialog(items: List<Owners>?, position: Int) {

        val builder = AlertDialog.Builder(requireContext(), R.style.PPKTheme_Alert).apply {
            setTitle("Внимание!")
            setMessage("Вы уверены, что хотите удалить владельца?\nВсе  данные будут удалены!")
            setPositiveButton("Да") { _, _ ->
                presenter.onRemoveClicked(items, position)
            }
            setNegativeButton(R.string.logOut_dialog_cancel) { button, _ ->
                button.dismiss()
            }
        }
        builder.show()
    }

    override fun setList(list: List<Owners>?) {
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
                offlineOwnersFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }
}