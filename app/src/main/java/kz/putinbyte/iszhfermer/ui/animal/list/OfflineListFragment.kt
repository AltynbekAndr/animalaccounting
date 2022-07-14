package kz.putinbyte.iszhfermer.ui.animal.list

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_add.*
import kz.putinbyte.iszhfermer.ui.animal.list.rv.IszhListAdapter
import kotlinx.android.synthetic.main.fragment_iszh_list.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.AnimalList
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.animal.list.OfflineListPresenter
import kz.putinbyte.iszhfermer.presentation.animal.list.OfflineListView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick

class OfflineListFragment : BaseFragment(), OfflineListView {

    override val layoutResId = R.layout.fragment_iszh_list

    companion object {
        fun newInstance() = OfflineListFragment()
    }

    @InjectPresenter
    lateinit var presenter: OfflineListPresenter
    private var ids =  ArrayList<Int>()

    @ProvidePresenter
    fun providePresenter(): OfflineListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
                .getInstance(OfflineListPresenter::class.java)
    }

    lateinit var adapter: IszhListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_iszh_list))
        initAdapter()
        setListeners()
    }

    private fun setListeners() {

        listCreateButton.setOnClickListener {
            presenter.animalRegister()
        }

        listGroupDeregisterButton.setOnClickListener {
            presenter.onDeregisterClicked(ids,injs = null)
        }

        listGroupSicknessesButton.setOnClickListener {
            presenter.setGroupSickness(ids)
        }

        listGroupResearchButton.setOnClickListener {
            presenter.setGroupResearch(ids)
        }

        listGroupPreventionButton.setOnClickListener {
            presenter.setGroupPrevention(ids)
        }


        listAnimalKindTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                    ) {
                        presenter.loadAnimalList(listAnimalKindTypeSpinner.items[position].id)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
    }

    private fun initAdapter() {
        adapter = IszhListAdapter(this::onItemClick, this::onItemLongClick, false, requireContext())
        listRec.adapter = adapter
        listRec.layoutManager =
                LinearLayoutManager(context)
    }

    override fun showErrorMessage(message: String) {
        try {
            Snackbar.make(
                clAddAnimalFragment,
                message,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }


    override fun setList(list: List<AnimalList.Animals>?) {
        adapter.items = list
    }

    override fun showAnimalKindType(list: List<BaseFormat>) {
        listAnimalKindTypeSpinner.items = BaseFormat.defaultData + list
    }

    override fun showProgressBar(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    fun onItemClick(items: List<AnimalList.Animals>, position: Int) {
        presenter.onItemClicked(items[position].id)
    }

    private fun onItemLongClick(
            items: List<AnimalList.Animals>, position: Int, isSelected: Boolean
    ) {
        ids.add(items[position].id)
        listGroupSicknessesButton.visibility = if (isSelected) View.VISIBLE else View.GONE
        listGroupResearchButton.visibility = if (isSelected) View.VISIBLE else View.GONE
        listGroupPreventionButton.visibility = if (isSelected) View.VISIBLE else View.GONE
        listGroupDeregisterButton.visibility = if (isSelected) View.VISIBLE else View.GONE
        listCreateButton.visibility = if (!isSelected) View.VISIBLE else View.GONE
        listAnimalKindTypeSpinner.visibility = if (!isSelected) View.VISIBLE else View.GONE
    }

}