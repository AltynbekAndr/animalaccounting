package kz.putinbyte.iszhfermer.ui.prevention.current

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
import kotlinx.android.synthetic.main.fragment_prevention.*
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.prevention.current.PreventionPresenter
import kz.putinbyte.iszhfermer.presentation.prevention.current.PreventionView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PreventionFragment : BaseFragment(), PreventionView {

    override val layoutResId = R.layout.fragment_prevention

    companion object {

        var animalID:ArrayList<Int>? = null
        fun newInstance() = PreventionFragment()

        private const val CURRENT_ANIMAL_KEY = "animalId"

        fun newInstance(animalId: ArrayList<Int>) = PreventionFragment().apply {
            arguments = Bundle().apply {
                putIntegerArrayList(CURRENT_ANIMAL_KEY, animalId)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: PreventionPresenter
    lateinit var fields: List<FieldData>

    @ProvidePresenter
    fun providePresenter(): PreventionPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(PreventionPresenter::class.java)
            .apply {
                animalsId = arguments?.getIntegerArrayList(CURRENT_ANIMAL_KEY)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_prevention))
        setListeners()

        fields = listOf(
            FieldData("ImmunType", preventionImmunTypeSpinner, FieldType.SPINNER),
            FieldData("SicknessType", preventionSicknessTypeSpinner, FieldType.SPINNER),
            FieldData("Doctor", preventionDoctorTypeSpinner, FieldType.SPINNER),
            FieldData("Date", preventionDateEdit, FieldType.DATE)
        )
    }

    override fun visibleReset(visible: Boolean) {
        preventionImmunTypeSpinner.visible(visible)
        preventionSicknessTypeSpinner.visible(visible)
        preventionDoctorTypeSpinner.visible(visible)
    }

    private fun setListeners() {

        val builder = setAsteriskMark()
        asteriskPreventionImmun.text = builder
        asteriskPreventionSickness.text = builder
        asteriskPreventionDoctor.text = builder
        asteriskPreventionDateEdit.text = builder

        preventionSaveButton.setOnClickListener {
            presenter.onSaveClicked()
        }

        preventionDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        preventionImmunTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.prevention.immunKindId = preventionImmunTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        preventionSicknessTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.prevention.sicknessId = preventionSicknessTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        preventionDoctorTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.prevention.doctorId = preventionDoctorTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    }

    override fun showImmunType(items: List<BaseFormat>) {
        preventionImmunTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите вид иммунизации", null))
        newList.addAll(items)
        preventionImmunTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.prevention.immunKindId }?.let {
            preventionImmunTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun showSicknessType(items: List<BaseFormat>) {
        preventionSicknessTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите заболевание", null))
        newList.addAll(items)
        preventionSicknessTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.prevention.sicknessId }?.let {
            preventionSicknessTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun showDoctorTypes(items: List<BaseFormat>) {
        preventionDoctorTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите врача", null))
        newList.addAll(items)
        preventionDoctorTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.prevention.doctorId }?.let {
            preventionDoctorTypeSpinner.setSelection(it.nameRu)
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


    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                addPreventionFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    override fun showSicknessData(prevention: AnimalPrevention){

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

    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                val backFormat = SimpleDateFormat(getString(R.string.app_back_date))
                val frontFormat = SimpleDateFormat(getString(R.string.app_front_date))
                preventionDateEdit.itemDateEdit.text =
                    frontFormat.format(calendar.time)
                presenter.prevention.immunizationDate = (backFormat.format(calendar.time))

            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}