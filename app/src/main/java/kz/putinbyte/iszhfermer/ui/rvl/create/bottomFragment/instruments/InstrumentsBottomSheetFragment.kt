package kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.instruments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import iszhfermer.R
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.instruments_message_fragment.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.db.DoctorTypes
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.presentation.rvl.create.CreateInventoryPresenter
import kz.putinbyte.iszhfermer.presentation.rvl.create.CreateInventoryView
import kz.putinbyte.iszhfermer.ui.rvl.create.rv.instruments.InstrumentsListAdapter
import kz.putinbyte.iszhfermer.ui.views.IszhSpinner
import kz.putinbyte.iszhfermer.utils.LogUtils
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.lang.Exception

class InstrumentsBottomSheetFragment(var listener: Listener, var item: List<BaseFormat>, var listElements: ArrayList<BaseFormat>) :
    BaseBottomSheetFragment(), CreateInventoryView {

    private lateinit var adapters: InstrumentsListAdapter

    private var listFields: ArrayList<BaseFormat> = ArrayList()

    override val layoutRes = R.layout.instruments_message_fragment

    @ProvidePresenter
    fun providePresenter(): CreateInventoryPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(CreateInventoryPresenter::class.java)
    }

    @InjectPresenter
    lateinit var presenter: CreateInventoryPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClick()

        view.post {
            val params: ViewGroup.LayoutParams = instrumentsRecyclerView.layoutParams
            if (item.size <= 1) {
                params.height = 250
            } else {
                params.height = 1350
            }
            instrumentsRecyclerView.layoutParams = params
        }
    }

    private fun initClick() {
        instrumentsCancelButton.setOnClickListener {
            dismiss()
        }

        instrumentsCreateSaveButton.setOnClickListener {
            if (listFields.size != 0) {
                listener.setOnClickListener(listFields)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Выберите хоть один элемент.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        instrumentsAddText.setOnClickListener {
            presenter.loadUnits()
        }
    }

    override fun initRecyclerView() {
        val list: ArrayList<ModelDisease> = arrayListOf()
        adapters = InstrumentsListAdapter(object : InstrumentsListAdapter.Listener {
            override fun setOnAddClickListener(item: ModelDisease) {
                if (listFields.firstOrNull { it == item.item } == null) {
                    item
                    item
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
            if (listElements.isNotEmpty()) {
                if (listElements.firstOrNull { it == itemMod } != null) {
                    list.add(ModelDisease(list.size, itemMod, true))
                } else {
                    list.add(ModelDisease(list.size, itemMod, false))
                }
            } else {
                list.add(ModelDisease(list.size, itemMod, false))
            }
            listFields = listElements

            adapters.update(list)
        }
        instrumentsRecyclerView.adapter = adapters
    }

    override fun showLoader(show: Boolean) {

    }

    override fun resetError() {

    }

    override fun showValidateError(error: Boolean, idName: String) {

    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    override fun showAddUnitsDialog(list: List<BaseFormat>) {

        var unitId: Int? = null

        val builder = AlertDialog.Builder(requireContext(), R.style.PPKTheme_Alert).create()
        val view = layoutInflater.inflate(R.layout.custom_instruments_dialog, null)
        builder.setTitle("Выберите единицу измерения")

        val cancelBtn = view.findViewById<Button>(R.id.instrumentsDialogCancelBtn)
        val okBtn = view.findViewById<Button>(R.id.instrumentsDialogCancelOk)
        val nameRu = view.findViewById<TextView>(R.id.instrumentsDialogNameRu)
        val nameKz = view.findViewById<TextView>(R.id.instrumentsDialogNameKz)
        val unitSpinner = view.findViewById<IszhSpinner>(R.id.instrumentsUnitSpinner)
        nameRu.hint = "Наименование"
        nameKz.hint = "Наименование на казахском"

        unitSpinner.items = list

        unitSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    unitId = unitSpinner.items[position].id!!
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        cancelBtn.setOnClickListener {
            builder.dismiss()
        }

        okBtn.setOnClickListener {
            val units = Units(null, nameKz.text.toString(), nameRu.text.toString(), unitId)

            if (empties(units.nameRu, units.nameKz)) {
                nameRu.background = ContextCompat.getDrawable(requireContext(), R.drawable.edt_bg)
                nameKz.background = ContextCompat.getDrawable(requireContext(), R.drawable.edt_bg)

                presenter.setUnits(units)
                builder.dismiss()

//                presenter.setDicRvlInstrument()

            } else {
                if (units.nameRu.isNotEmpty()) {
                    nameRu.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.edt_bg)
                } else {
                    nameRu.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_validate)
                }

                if (units.nameKz.isNotEmpty()) {
                    nameKz.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.edt_bg)
                } else {
                    nameKz.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_validate)
                }
            }
        }

        builder.setCancelable(false)
        builder.setView(view)
        builder.show()

    }

    private fun empties(nameRu: String, nameKz: String): Boolean {
        if (nameRu.isNotEmpty() && nameKz.isNotEmpty()) {
            return true
        }
        return false
    }

    interface Listener {
        fun setOnClickListener(item: ArrayList<BaseFormat>)
    }

    override fun showResearchType(item: List<ReceivedSampleNameModel>) {

    }

    override fun showResultType(item: List<DoctorTypes>) {

    }

    override fun showDoctorType(item: List<DicRvlBranchModel>) {

    }

    override fun showDiseaseType(item: List<BaseFormat>, result: List<SicknessKindItem>) {

    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                bottomInstrumentsFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    //Добовляем новые данные в список
    override fun showMaterialsType(
        item: List<BaseFormat>,
        result: List<DicRvlInstrumentModel>
    ) {
        val list: ArrayList<ModelDisease> = arrayListOf()
        item.forEach {
            list.add(ModelDisease(0,it, true ))
        }
        instrumentsRecyclerView.adapter = adapters
    }

}