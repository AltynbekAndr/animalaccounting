package kz.putinbyte.iszhfermer.ui.rvl.bottomFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_search_message_dialog.*
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.ListAnimalsModel
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.presentation.searchListView.SearchListPresenter
import kz.putinbyte.iszhfermer.presentation.searchListView.SearchListView
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.rvl.rv.SaveSearchAdapter
import kz.putinbyte.iszhfermer.ui.rvl.rv.SearchAdapter
import kz.putinbyte.iszhfermer.utils.AlinNotification.animFlashing
import kz.putinbyte.iszhfermer.utils.AlinNotification.isStopAnim
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

class SearchMessageBottomSheetFragment(var saveListener: Listener) : BaseBottomSheetFragment(),
    SearchListView {

    private var arList: ArrayList<ListModel> = arrayListOf()

    private val sheetFilling: ArrayList<ListModel> = arrayListOf()
    private var addingSelectedItems = MutableLiveData<ArrayList<ListModel>>()

    override val layoutRes = R.layout.fragment_search_message_dialog

    @InjectPresenter
    lateinit var presenter: SearchListPresenter

    @ProvidePresenter
    fun providePresenter(): SearchListPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(SearchListPresenter::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
        initDataTransaction()
    }

    fun toast(test: String) = Toast.makeText(requireContext(), test, Toast.LENGTH_LONG).show()

    private fun initDataTransaction() {
        addingSelectedItems.observe(this) {
            if (it.size != 0) {
                animFlashing(selectedItemsIm)
            } else {
                isStopAnim(selectedItemsIm)
            }
        }
    }

    private var listener = object : SearchAdapter.Listener {
        override fun setOnClickListener(boolean: Boolean, item: ListModel) {
            if (boolean) {
                sheetFilling.add(item)
            } else {
                sheetFilling.remove(item)
            }
            selectedItemsIm.text = sheetFilling.size.toString()
            addingSelectedItems.value = sheetFilling
        }
    }

    private var listenerSave = object : SaveSearchAdapter.Listener {
        override fun setOnClickListener(boolean: Boolean, item: ListModel) {
            if (addingSelectedItems.value!!.toString() != "null"){
                val itemClear: ArrayList<ListModel> = addingSelectedItems.value!!
                if (!boolean){
                    itemClear.remove(item)
                    sheetFilling.remove(item)
                    addingSelectedItems.value = itemClear
                    cleatItem()
                }
            }
        }
    }

    fun cleatItem(){
        selectedItemsIm.text = sheetFilling.size.toString()
        adaptersSave.update(addingSelectedItems.value!!)
    }

    private val adapters = SearchAdapter(listener)
    private val adaptersSave = SaveSearchAdapter(listenerSave)

    private fun initClick() {
        saveListBtn.setOnClickListener {
            if (addingSelectedItems.value.toString() != "null"){
                if ( addingSelectedItems.value!!.size != 0){
                    saveListener.setOnClickListener(addingSelectedItems.value!!)
                    dismiss()
                }else{
                    toast("Список Пуст")
                }
            }else{
                toast("Список Пуст")
            }
        }

        clearListBtn.setOnClickListener {
            if (addingSelectedItems.value.toString() != "null"){
                arList.clear()
            }
            dismiss()
        }

        iayItemsIm.setOnClickListener {
            if (addingSelectedItems.value.toString() != "null"){
                searchTextView.setText("")
                searchTextView.isEnabled = false
                iayItemsIm.isVisible = false
                imageClosed.isVisible = true
                adaptersSave.update(addingSelectedItems.value!!)
                searchRecyclerView.adapter = adaptersSave
            }else{
                toast("Список Пуст")
            }
        }

        imageClosed.setOnClickListener {
            if (addingSelectedItems.value.toString() != "null"){
                imageClosed.isVisible = false
                iayItemsIm.isVisible = true
                searchTextView.isEnabled = true
                adaptersSave.update(ArrayList<ListModel>())
                searchRecyclerView.adapter = adaptersSave
            }else{
                toast("Список Пуст")
            }
        }

        searchTextView.addTextChangedListener {
            val symbol = it!!.contains("KZ")
            if (symbol) {
                if (it.length == 12 || it.length == 11) {
                    presenter.gettingListAnimals(inj = it.toString())
                } else if (it.length == 12 || it.length == 11) {
                    conRepetition()
                }
            } else {
                if (it.length == 12 || it.length == 11) {
                    presenter.gettingListAnimals(identifier = it.toString())
                } else if (it.length == 12 || it.length == 11) {
                    conRepetition()
                }
            }
        }
    }

    private fun conRepetition() {
        arList.clear()
        adapters.update(arList)
    }

    override fun getList(list: ListAnimalsModel?) {
        arList = list!!.lists as ArrayList<ListModel>
        if (list.lists!!.isNotEmpty()) {
            if (addingSelectedItems.value != null) {
                for (items in list.lists!!) {
                    if (addingSelectedItems.value!!.size != 0) {
                        val result =
                            addingSelectedItems.value!!.firstOrNull { it.inj == items.inj && it.id == items.id }
                        if (result == null) {
                            adapters.update(arList)
                        }
                    }
                }
            } else {
                adapters.update(arList)
            }
            searchRecyclerView.adapter = adapters
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    interface Listener{
        fun setOnClickListener(item: ArrayList<ListModel>)
    }
}