package kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.desease

import android.os.Bundle
import android.view.View
import android.widget.Toast
import iszhfermer.R
import kotlinx.android.synthetic.main.disease_message_fragment.*
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.ModelDisease
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.rvl.create.rv.desease.DiseaseListAdapter
import android.view.ViewGroup

class DiseaseBottomSheetFragment(var listener: Listener, var item: List<BaseFormat>, var listElements: ArrayList<BaseFormat>) :
    BaseBottomSheetFragment() {

    private var listFields: ArrayList<BaseFormat> = ArrayList()

    override val layoutRes = R.layout.disease_message_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClick()


        view.post {
            val params: ViewGroup.LayoutParams = diseaseRecyclerView.layoutParams
            if (item.size <= 1){
                params.height = 250
            }else{
                params.height = 1350
            }
            diseaseRecyclerView.layoutParams = params
        }
    }

    private fun initClick() {
        issuedCancelButton.setOnClickListener {
            dismiss()
        }

        createSaveDiseaseButton.setOnClickListener {
            if (listFields.size != 0) {
                listener.setOnClickListener(listFields)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Выберите хоть один элемент.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initRecyclerView() {
        val list: ArrayList<ModelDisease> = arrayListOf()
        val adapters = DiseaseListAdapter(object : DiseaseListAdapter.Listener {
            override fun setOnAddClickListener(item: ModelDisease) {
                if (listFields.firstOrNull { it == item.item } == null) {
                    listFields.add(item.item)
                }
            }

            override fun setOnDeleteClickListener(item: ModelDisease) {
                if (listFields.firstOrNull { it == item.item } != null) {
                    listFields.remove(item.item)
                }
            }
        })

        for (itemMod in item) {
            if (listElements.isNotEmpty()){
                if (listElements.firstOrNull { it == itemMod} != null){
                    list.add(ModelDisease(list.size, itemMod, true))
                }else{
                    list.add(ModelDisease(list.size, itemMod, false))
                }
            }else{
                list.add(ModelDisease(list.size, itemMod, false))
            }
            listFields = listElements
            adapters.update(list)
        }
        diseaseRecyclerView.adapter = adapters
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    interface Listener {
        fun setOnClickListener(item: ArrayList<BaseFormat>)
    }
}