package kz.putinbyte.iszhfermer.ui.individuals.juridicall

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_juridical.*
import kotlinx.android.synthetic.main.item_date.view.*
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
import kz.putinbyte.iszhfermer.presentation.individuals.juridicall.JuridicalPresenter
import kz.putinbyte.iszhfermer.presentation.individuals.juridicall.JuridiicalView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.search.dialogRegions.RegionsBottomSheetFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class JuridicallListFragment : BaseFragment(), JuridiicalView {

    override val layoutResId = R.layout.fragment_juridical

    companion object {

        fun newInstance() = JuridicallListFragment()
    }

    @InjectPresenter
    lateinit var presenter: JuridicalPresenter

    @ProvidePresenter
    fun providePresenter(): JuridicalPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(JuridicalPresenter::class.java)
    }

    lateinit var fields: List<FieldData>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        fields = listOf(
            FieldData("bin", addOwnerJuridicalBin, FieldType.INPUT),
            FieldData("fio", addOwnerNameRu, FieldType.INPUT),
            FieldData("shortName", addOwnerShortNameRu, FieldType.INPUT),
            FieldData("property", addOwnerPropertyType, FieldType.SPINNER),
            FieldData("production", addOwnerProductionSpinner, FieldType.SPINNER),
            FieldData("email", addOwnerJuridicalEmail, FieldType.INPUT),
            FieldData("date", addOwnerJuridicalDocumentDateIssue, FieldType.DATE),
            FieldData("kato", addOwnerJuridicalKatoId, FieldType.INPUT)
        )

    }

    private fun initListeners(){

        asterisksFields()

        addOwnerJuridicalBin.afterTextChanged { presenter.juridical.bin = it }
        addOwnerNameRu.afterTextChanged { presenter.juridical.nameRu = it }
        addOwnerShortNameRu.afterTextChanged { presenter.juridical.shortNameRu = it }
        addOwnerChiefFullNam.afterTextChanged { presenter.juridical.chiefFullName = it }
        addOwnerChiefPosition.afterTextChanged { presenter.juridical.chiefPosition = it }
        addOwnerJuridicalEmail.afterTextChanged { presenter.juridical.email = it }
        addOwnerJuridicalTel.afterTextChanged { presenter.juridical.tel = it }
        addOwnerJuridicalMobilePhone.afterTextChanged { presenter.juridical.mobilePhone = it }
        addOwnerJuridicalDocumentNumber.afterTextChanged { presenter.juridical.documentNumber = it }
        addOwnerJuridicalDocumentIssueBy.afterTextChanged { presenter.juridical.doumentIssueBy = it }
        addOwnerBik.afterTextChanged { presenter.juridical.bik = it }
        addOwnerOkpoCode.afterTextChanged { presenter.juridical.okpoCode = it }
        addOwnerJuridicalPostIndex.afterTextChanged { presenter.juridical.postIndex = it }
        addOwnerJuridicalStreet.afterTextChanged { presenter.juridical.street = it }
        addOwnerJuridicalHouse.afterTextChanged { presenter.juridical.house = it }
        addOwnerJuridicalFlat.afterTextChanged { presenter.juridical.flat = it }

        addOwnerEnterpriseTypeId.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.juridical.enterpriseTypeId = addOwnerEnterpriseTypeId.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addOwnerPropertyType.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.juridical.opfId = addOwnerPropertyType.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addOwnerProductionSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.juridical.okedId = addOwnerProductionSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addOwnerJuridicalDocumentDateIssue.itemDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }

        addOwnerJuridicalKatoId.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertRegions()
            }, 500)
        }

        juridicalCreatesButton.setOnClickListener {
            presenter.onSaveClicked()
        }

    }

    private fun showAlertRegions() {
        val bottomSheetDialogFragment =
            RegionsBottomSheetFragment(object : RegionsBottomSheetFragment.Listener {
                override fun setOnClick(items: Region.AnimalAmountByKato) {
                    addOwnerJuridicalKatoId.text = items.name
                    presenter.juridical.katoId = (items.id)
                }
            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        addOwnerJuridicalKatoId.isEnabled = true
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun visibleReset(visible: Boolean) {
        addOwnerEnterpriseTypeId.visible(visible)
        addOwnerPropertyType.visible(visible)
        addOwnerProductionSpinner.visible(visible)
    }

    override fun resetError() {
        presenter.validateError = false
        baseResetError(fields)
    }

    override fun showValidateError(error: Boolean, fieldKey: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields,fieldKey)
        }
    }

    override fun showEnterpriseType(result: List<BaseFormat>) {
        addOwnerEnterpriseTypeId.visible(!result.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите тип", null))
        newList.addAll(result)
        addOwnerEnterpriseTypeId.items = newList
    }

    override fun showProduction(result: List<BaseFormat>) {
        addOwnerProductionSpinner.visible(!result.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите вид", null))
        newList.addAll(result)
        addOwnerProductionSpinner.items  =newList
    }

    override fun showPropertyType(result: List<BaseFormat>) {
        addOwnerPropertyType.visible(!result.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите вид", null))
        newList.addAll(result)
        addOwnerPropertyType.items = newList
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                juridicalFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

    fun showDatePickerDialog(isStart: Boolean) {
        val calendar = Calendar.getInstance()
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)
        val backFormat = SimpleDateFormat(getString(R.string.app_back_date))
        val frontFormat = SimpleDateFormat(getString(R.string.app_front_date))
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                if (isStart) {
                    addOwnerJuridicalDocumentDateIssue.itemDateEdit.text =
                        frontFormat.format(calendar.time)
                    presenter.juridical.documentDateIssue = backFormat.format(calendar.time)
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
        asteriskBin.text = builder
        asteriskOwnerNameRu.text = builder
        asteriskOwnerShortNameRu.text = builder
        asteriskPropertyType.text = builder
        asteriskProductionSpinneru.text = builder
        asteriskJuridicalEmail.text = builder
        asteriskDocumentDateIssue.text = builder
        asteriskJuridicalKatoId.text = builder
    }
}