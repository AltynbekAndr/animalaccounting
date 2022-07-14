package kz.putinbyte.iszhfermer.ui.research.create

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_research.*
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.research.create.ResearchPresenter
import kz.putinbyte.iszhfermer.presentation.research.create.ResearchView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class ResearchFragment : BaseFragment(), ResearchView {

    override val layoutResId = R.layout.fragment_research

    companion object {

        fun newInstance() = ResearchFragment()

        private const val CURRENT_ANIMAL_KEY = "animalId"

        fun newInstance(animalId: ArrayList<Int>) = ResearchFragment().apply {
            arguments = Bundle().apply {
                putIntegerArrayList(CURRENT_ANIMAL_KEY, animalId)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: ResearchPresenter
    lateinit var fields: List<FieldData>

    @ProvidePresenter
    fun providePresenter(): ResearchPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(ResearchPresenter::class.java)
            .apply {
                animalsId = arguments?.getIntegerArrayList(CURRENT_ANIMAL_KEY)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_research))
        initListeners()

        fields = listOf(
            FieldData("SicknessType", researchSicknessTypeSpinner, FieldType.SPINNER),
            FieldData("researchType", researchTypeSpinner, FieldType.SPINNER),
            FieldData("Result", researchResultTypeSpinner, FieldType.SPINNER),
            FieldData("Doctor", researchDoctorTypeSpinner, FieldType.SPINNER),
            FieldData("Date", researchDateEdit, FieldType.DATE)
        )
    }

    private fun initListeners() {

        val builder = setAsteriskMark()
        asteriskResearchSickness.text = builder
        asteriskResearchSpinner.text = builder
        asteriskResearchResult.text = builder
        asteriskResearchDoctor.text = builder
        asteriskResearchDate.text = builder

        researchSaveButton.setOnClickListener {
            presenter.onSaveButtonClicked()
        }

        researchDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        researchCommentEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                presenter.research.comment = s.toString()
            }
        })

        researchSicknessTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.research.sicknessId = researchSicknessTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        researchTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.research.researchKindId = researchTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        researchResultTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.research.sicknessCause =
                        researchResultTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        researchDoctorTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.research.doctorId = researchDoctorTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

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

    override fun showSicknessType(items: List<BaseFormat>) {
        researchSicknessTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите заболевание", null))
        newList.addAll(items)
        researchSicknessTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.research.sicknessId }?.let {
            researchSicknessTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun showResearchType(items: List<BaseFormat>) {
        researchTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите исследование", null))
        newList.addAll(items)
        researchTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.research.researchKindId }?.let {
            researchTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun showDoctorsType(items: List<BaseFormat>) {
        researchDoctorTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите врача", null))
        newList.addAll(items)
        researchDoctorTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.research.doctorId }?.let {
            researchDoctorTypeSpinner.setSelection(it.nameRu)
        }
    }
    override fun showResultType(items: List<BaseFormat>) {
        researchResultTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите результат", null))
        newList.addAll(items)
        researchResultTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.research.sicknessCause }?.let {
            researchResultTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun visibleReset(visible: Boolean) {
        researchSicknessTypeSpinner.visible(visible)
        researchTypeSpinner.visible(visible)
        researchDoctorTypeSpinner.visible(visible)
        researchResultTypeSpinner.visible(visible)
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showValidateError(error: Boolean, fieldKey: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields,fieldKey)
        }
    }

    override fun showResearchData(research: AnimalResearch) {

    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                addResearchFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }
    }

    override fun resetError(){
        presenter.validateError = false
        baseResetError(fields)
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
                researchDateEdit.itemDateEdit.text =
                    frontFormat.format(calendar.time)
                presenter.research.sicknessRegDate = backFormat.format(calendar.time)

            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

}