package kz.putinbyte.iszhfermer.ui.rvl.bottomFragment

import android.os.Bundle
import android.view.View
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_search_save_message_dialog.*
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.rvl.rv.SaveSearchAdapter

class SaveMessageBottomSheetFragment(var saveListener: Listener? = null, var items: ArrayList<ListModel>) : BaseBottomSheetFragment(){

    private lateinit var adapters : SaveSearchAdapter

    override val layoutRes = R.layout.fragment_search_save_message_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClick()
    }

    private fun initClick() {
        messageSavedDataBtn.setOnClickListener {
            saveListener!!.savedData()
            dismiss()
        }
    }

    private fun initRecyclerView() {
        if (items.size == 0){
            saveTextTitle.text = "Выбранных данных нет"
        }

        val list: ArrayList<ListModel> = items
        adapters = SaveSearchAdapter(object : SaveSearchAdapter.Listener{
            override fun setOnClickListener(boolean: Boolean, item: ListModel) {
                saveListener!!.setOnClearClickListener(boolean, item)
                list.remove(item)
                adapters.update(list)
                recyclerView.adapter = adapters
            }
        })
        adapters.update(items)
        recyclerView.adapter = adapters
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    interface Listener{
        fun savedData()
        fun setOnClearClickListener(boolean: Boolean, item: ListModel)
    }
}