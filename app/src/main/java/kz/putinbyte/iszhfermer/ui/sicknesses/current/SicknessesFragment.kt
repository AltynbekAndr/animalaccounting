package kz.putinbyte.iszhfermer.ui.sicknesses.current

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_sickness.*
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.sicknesses.current.SicknessesPresenter
import kz.putinbyte.iszhfermer.presentation.sicknesses.current.SicknessesView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SicknessesFragment : BaseFragment(), SicknessesView {

    override val layoutResId = R.layout.fragment_sickness

    companion object {

        fun newInstance() = SicknessesFragment()

        private const val CURRENT_SICKNESS_KEY = "ids"

        fun newInstance(ids: ArrayList<Int>?) = SicknessesFragment().apply {
            arguments = Bundle().apply {
                if (ids != null) {
                    putIntegerArrayList(CURRENT_SICKNESS_KEY, ids)
                }
            }
        }
    }

    @ProvidePresenter
    fun providePresenter(): SicknessesPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(SicknessesPresenter::class.java)
            .apply {
                animalsId = arguments?.getIntegerArrayList(CURRENT_SICKNESS_KEY)
            }
    }

    lateinit var fields : List<FieldData>

    @InjectPresenter
    lateinit var presenter: SicknessesPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_sicknesses))
        setListeners()

        fields = listOf(
            FieldData("Date", sicknessDateEdit, FieldType.DATE),
            FieldData("Doctor", sicknessDoctorTypeSpinner, FieldType.SPINNER),
            FieldData("Conclusion", sicknessConclusionTypeSpinner, FieldType.SPINNER),
            FieldData("Diagnostic", sicknessFirstDiagnosticTypeSpinner, FieldType.SPINNER)
        )
    }

    private fun setListeners() {

        val builder = setAsteriskMark()
        asteriskSicknessFirstSpinner.text = builder
        asteriskSicknessConclusionSpinner.text = builder
        asteriskSicknessDoctorSpinner.text = builder
        asteriskSicknessDate.text = builder

        sicknessDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        sicknessSaveButton.setOnClickListener {
            presenter.onSaveClicked()
        }

        sicknessFirstDiagnosticTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.sickness.initialDiagnosisId = sicknessFirstDiagnosticTypeSpinner.items[position].id!!

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        sicknessConclusionTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.sickness.sicknessCause = sicknessConclusionTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        sicknessDoctorTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.sickness.doctorId = sicknessDoctorTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showConclusionType(items: List<BaseFormat>) {
        sicknessConclusionTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите исход", null))
        newList.addAll(items)
        sicknessConclusionTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.sickness.sicknessCause }?.let {
            sicknessConclusionTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun showDoctorType(items: List<BaseFormat>) {
        sicknessDoctorTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите врача", null))
        newList.addAll(items)
        sicknessDoctorTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.sickness.doctorId }?.let {
            sicknessDoctorTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun showFirstDiagnosisType(items: List<BaseFormat>) {
        sicknessFirstDiagnosticTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите диагноз", null))
        newList.addAll(items)
        sicknessFirstDiagnosticTypeSpinner.items = newList
        items.firstOrNull{ it.id == presenter.sickness.initialDiagnosisId}?.let {
            sicknessFirstDiagnosticTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun visibleReset(visible: Boolean) {
        sicknessConclusionTypeSpinner.visible(visible)
        sicknessDoctorTypeSpinner.visible(visible)
        sicknessFirstDiagnosticTypeSpinner.visible(visible)
    }

    override fun showSicknessData(sickness: AnimalSickness){

    }

    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                val backFormat = SimpleDateFormat(getString(R.string.app_back_date))
                val frontFormat = SimpleDateFormat(getString(R.string.app_front_date))
                sicknessDateEdit.itemDateEdit.text =
                    frontFormat.format(calendar.time)
                presenter.sickness.sicknessRegDate = backFormat.format(calendar.time)
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                addSicknessFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    override fun showValidateError(error: Boolean, fieldKey: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields,fieldKey)
        }
    }

    override fun resetError(){
        presenter.validateError = false
        baseResetError(fields)
    }

    private fun setAsteriskMark(): SpannableStringBuilder {
        val colored = "*"
        val builder = SpannableStringBuilder()
        builder.append()
        val start = builder.length
        builder.append(colored)
        val end = builder.length
        builder.setSpan(
            ForegroundColorSpan(Color.RED), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }
}