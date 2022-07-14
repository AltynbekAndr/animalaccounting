package kz.putinbyte.iszhfermer.ui.individuals.physical

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_physical.*
import kotlinx.android.synthetic.main.item_date_current.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.extensions.afterTextChanged
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.individuals.physical.PhysicalPresenter
import kz.putinbyte.iszhfermer.presentation.individuals.physical.PhysicalView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.search.dialogRegions.RegionsBottomSheetFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class PhysicalListFragment : BaseFragment(), PhysicalView {

    override val layoutResId = R.layout.fragment_physical

    companion object {

        fun newInstance() = PhysicalListFragment()
    }

    @InjectPresenter
    lateinit var presenter: PhysicalPresenter

    @ProvidePresenter
    fun providePresenter(): PhysicalPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(PhysicalPresenter::class.java)
    }

    lateinit var fields: List<FieldData>
    var iinDisableForProperty = false
    var iinDisableForEdit = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainView).setTitle(getString(R.string.addindivid))
        initListeners()

        fields = listOf(
            FieldData("iin", addOwnerINN, FieldType.INPUT),
            FieldData("lastName", addOwnerLastName, FieldType.INPUT),
            FieldData("firstName", addOwnerFirstName, FieldType.INPUT),
            FieldData("middleName", addOwnerMiddleName, FieldType.INPUT),
            FieldData("birthDate", addOwnerBirthDateEdit, FieldType.DATE),
            FieldData("kato", addOwnerKatoId, FieldType.INPUT),
            FieldData("email", addOwnerEmail, FieldType.INPUT),
            FieldData("documentNum", addOwnerDocumentNumber, FieldType.INPUT),
            FieldData("Country", addOwnerCitizenshipId, FieldType.SPINNER),
            FieldData("date", addOwnerDocumentDateIssue, FieldType.DATE),
            FieldData("issuedBy", addOwnerDocumentIssueBy, FieldType.INPUT)
        )
    }

    private fun initListeners() {

        addOwnerINN.afterTextChanged {
            presenter.physical.iin = it
            presenter.getEditLength(it)
        }
        addOwnerLastName.afterTextChanged { presenter.physical.lastName = it }
        addOwnerFirstName.afterTextChanged { presenter.physical.firstName = it }
        addOwnerNameRu.afterTextChanged { presenter.physical.firstName = it }
        addOwnerMiddleName.afterTextChanged { presenter.physical.middleName = it }
        addOwnerEmail.afterTextChanged { presenter.physical.email = it }
        addOwnerTel.afterTextChanged { presenter.physical.tel = it }
        addOwnerMobilePhone.afterTextChanged { presenter.physical.mobilePhone = it }
        addOwnerDocumentNumber.afterTextChanged { presenter.physical.documentNumber = it }
        addOwnerDocumentIssueBy.afterTextChanged { presenter.physical.doumentIssueBy = it }
        addOwnerPostIndex.afterTextChanged { presenter.physical.postIndex = it }
        addOwnerStreet.afterTextChanged { presenter.physical.street = it }
        addOwnerHouse.afterTextChanged { presenter.physical.house = it }
        addOwnerFlat.afterTextChanged { presenter.physical.flat = it }

        physicalCreatesButton.setOnClickListener {
            presenter.onSaveClicked()
        }

        addOwnerBirthDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }

        addOwnerDocumentDateIssue.itemDateButton.setOnClickListener {
            showDatePickerDialog(false)
        }

        iinFindButton.setOnClickListener {
            presenter.onInnClicked()
        }

        addOwnerKatoId.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertRegions()
            }, 500)
        }

        addOwnerCitizenshipId.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.physical.citizenshipId = addOwnerCitizenshipId.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addOwnerTypeProperty.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.onTypeChanged(addOwnerTypeProperty.items[position].id)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        asterisksFields()
    }


    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showCountryOrigin(result: List<BaseFormat>) {
        addOwnerCitizenshipId.visible(!result.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите страну", null))
        newList.addAll(result)
        addOwnerCitizenshipId.items = newList
    }

    override fun showPropertyType(propertyType: List<BaseFormat>) {
        addOwnerTypeProperty.visible(!propertyType.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите вид", null))
        newList.addAll(propertyType)
        addOwnerTypeProperty.items = newList
    }

    override fun getEditLength(it: String) {
        iinFindButton.background = if (it.length == 12 && iinDisableForProperty)
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_primary) else
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_danger_disabled)
        iinDisableForEdit = it.length == 12
        iinFindButton.isEnabled = it.length == 12 && iinDisableForProperty
    }

    override fun onTypeChanged(id: Int) {
        iinDisableForProperty = id == 11
        iinFindButton.isEnabled = id == 11 && iinDisableForEdit

        iinFindButton.background = if (id == 11 && iinDisableForEdit)
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_primary) else
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_danger_disabled)

        addOwnerFirstName.visible(id == 11)
        addOwnerLastName.visible(id == 11)
        addOwnerMiddleName.visible(id == 11)
        addOwnerNameRu.visible(id != 11)
        asteriskOwnerNameRu.visible(id != 11)

        addOwnerFirstName.isEnabled = id != 11
        addOwnerLastName.isEnabled = id != 11
        addOwnerMiddleName.isEnabled = id != 11
        addOwnerBirthDateEdit.itemDateButton.isEnabled = id != 11
        addOwnerDocumentNumber.isEnabled = id != 11
        addOwnerDocumentDateIssue.itemDateButton.isEnabled = id != 11
        addOwnerDocumentIssueBy.isEnabled = id != 11
        addOwnerCitizenshipId.itemSpinnerSpinner.isEnabled = id != 11
        addOwnerStreet.isEnabled = id != 11
        addOwnerHouse.isEnabled = id != 11
        addOwnerFlat.isEnabled = id != 11

        addOwnerLastName.background = changeBackground(id == 11)
        addOwnerFirstName.background = changeBackground(id == 11)
        addOwnerMiddleName.background = changeBackground(id == 11)
        addOwnerStreet.background = changeBackground(id == 11)
        addOwnerHouse.background = changeBackground(id == 11)
        addOwnerFlat.background = changeBackground(id == 11)
        addOwnerDocumentNumber.background = changeBackground(id == 11)
        addOwnerDocumentIssueBy.background = changeBackground(id == 11)

        addOwnerBirthDateEdit.itemDateButton.background = if (id == 11)
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_danger_disabled) else
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_primary)

        addOwnerDocumentDateIssue.itemDateButton.background = if (id == 11)
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_danger_disabled) else
            ContextCompat.getDrawable(requireContext(), R.drawable.btn_primary)

        addOwnerCitizenshipId.itemSpinnerSpinner.background = if (id == 11)
            ContextCompat.getDrawable(requireContext(), R.drawable.spinner_bg_disabled) else
            ContextCompat.getDrawable(requireContext(), R.drawable.spinner_bg)

    }

    private fun changeBackground(isProperty: Boolean): Drawable? {
        return if (isProperty) ContextCompat.getDrawable(
            requireContext(),
            R.drawable.bg_edit_disable
        ) else
            ContextCompat.getDrawable(requireContext(), R.drawable.edt_bg)
    }

    override fun visibleReset(visible: Boolean) {
        addOwnerCitizenshipId.visible(visible)
    }

    override fun resetError() {
        presenter.validateError = false
        baseResetError(fields)
    }

    override fun showValidateError(error: Boolean, fieldKey: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields, fieldKey)
        }
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                physicalFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

    private fun showAlertRegions() {
        val bottomSheetDialogFragment =
            RegionsBottomSheetFragment(object : RegionsBottomSheetFragment.Listener {
                override fun setOnClick(items: Region.AnimalAmountByKato) {
                    addOwnerKatoId.text = items.name
                    presenter.physical.katoId = (items.id)
                }
            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        addOwnerKatoId.isEnabled = true
    }

    fun showDatePickerDialog(isStart: Boolean) {
        val calendar = Calendar.getInstance()
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)
        val backFormat = SimpleDateFormat(getString(R.string.app_back_date), Locale.getDefault())
        val frontFormat = SimpleDateFormat(getString(R.string.app_front_date), Locale.getDefault())
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                if (isStart) {
                    addOwnerBirthDateEdit.itemDateEdit.text =
                        frontFormat.format(calendar.time)
                    presenter.physical.birthDate = backFormat.format(calendar.time)
                } else {
                    addOwnerDocumentDateIssue.itemDateEdit.text =
                        frontFormat.format(calendar.time)
                    presenter.physical.documentDateIssue = backFormat.format(calendar.time)
                }
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
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

    private fun asterisksFields() {
        val builder = setAsteriskMark()
        asteriskOwnerFirstName.text = builder
        asteriskOwnerLastName.text = builder
        asteriskOwnerINN.text = builder
        asteriskOwnerKatoSpinner.text = builder
        asteriskOwnerBirthDateEdit.text = builder
        asteriskaOwnerDocumentNumber.text = builder
        asteriskaOwnerDocumentDateIssue.text = builder
        asteriskaOwnerDocumentIssueBy.text = builder
        asteriskaOwnerCitizenshipId.text = builder
        asteriskaOwnerMiddleName.text = builder
        asteriskOwnerNameRu.text = builder
    }

}