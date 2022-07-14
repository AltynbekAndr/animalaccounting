package kz.putinbyte.iszhfermer.ui.deposit.create

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_deposit.*
import kotlinx.android.synthetic.main.item_date.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.extensions.afterTextChanged
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.presentation.deposit.current.DepositePresenter
import kz.putinbyte.iszhfermer.presentation.deposit.current.DepositView
import kz.putinbyte.iszhfermer.ui.add.owners.OwnersBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import kz.putinbyte.iszhfermer.utils.MyUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class DepositFragment : BaseFragment(), DepositView {

    override val layoutResId = R.layout.fragment_deposit

    companion object {

        fun newInstance() = DepositFragment()

        private const val CURRENT_DEPOSIT_KEY = "animalId"

        fun newInstance(animalId: Int?) = DepositFragment().apply {
            arguments = Bundle().apply {
                if (animalId != null) {
                    putInt(CURRENT_DEPOSIT_KEY, animalId)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: DepositePresenter
    lateinit var fields: List<FieldData>

    @ProvidePresenter
    fun providePresenter(): DepositePresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(DepositePresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_DEPOSIT_KEY)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_deposit))
        setListeners()

        fields = listOf(
            FieldData("StartDate", depositStartDateEdit, FieldType.DATE),
            FieldData("EndDate", depositEndDateEdit, FieldType.DATE),
            FieldData("Borrower", depositBorrowerButton, FieldType.INPUT),
            FieldData("Pledgee", depositPledgeeButton, FieldType.INPUT),
            FieldData("RegisterNum", depositRegisterNumberEdit, FieldType.INPUT),
            FieldData("ContractNum", depositContractNumberEdit, FieldType.INPUT),
            FieldData("Summ", depositLoanAmountEdit, FieldType.INPUT),

            FieldData("File", depositFileUploadButton, FieldType.FILE)
        )
    }

    private fun setListeners() {

        val mGetContent: ActivityResultLauncher<String> = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null)
                presenter.onUploadFileClicked(uri)
        }

        depositRegisterNumberEdit.afterTextChanged { presenter.deposit.registerNumber = it }
        depositContractNumberEdit.afterTextChanged { presenter.deposit.contractNumber = it  }
        depositLoanAmountEdit.afterTextChanged { presenter.deposit.pledgeSum= it.toDouble()  }
        depositCommentEdit.afterTextChanged { presenter.deposit.note = it }

        depositStartDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }

        depositEndDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog(false)
        }

        depositBorrowerButton.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertOwners(true)
            }, 500)
        }

        depositPledgeeButton.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertOwners(false)
            }, 500)
        }

        depositFileUploadButton.setOnClickListener {
            withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, {
                mGetContent.launch("application/*")
            }) {
                presenter.onPermissionDenied(getString(R.string.app_audio_permission_denied))
            }
        }
        depositSaveButton.setOnClickListener {
            presenter.onSaveClicked()
        }

        asterisksFields()
    }

    fun showDatePickerDialog(start: Boolean) {
        val calendar = Calendar.getInstance()
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)
        val frontFormat = SimpleDateFormat(getString(R.string.app_front_date))
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(year, month, day)
                if (start) {
                    depositStartDateEdit.itemDateEdit.text =
                        frontFormat.format(calendar.time)
                    presenter.deposit.contractStartDate =
                        MyUtils.tuServerDate(frontFormat.format(calendar.time))
                } else {
                    depositEndDateEdit.itemDateEdit.text =
                        frontFormat.format(calendar.time)
                    presenter.deposit.contractEndDate =
                        MyUtils.tuServerDate(frontFormat.format(calendar.time))
                }

            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun asterisksFields() {
        val builder = setAsteriskMark()
        depositOwner.text = builder
        depositPledger.text = builder
        depositRegisterNum.text = builder
        depositContractNum.text = builder
        depositStartDate.text = builder
        depositEndDate.text = builder
        depositSumm.text = builder
        depositFile.text = builder
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

    private fun showAlertOwners(isBorrower:Boolean) {
        val bottomSheetDialogFragment =
            OwnersBottomSheetFragment(object : OwnersBottomSheetFragment.Listener {
                override fun setOnClick(items: Owners) {
                    if (isBorrower){
                        if (items.fullName != null){
                            depositBorrowerButton.text = items.fullName
                        }else{
                            depositBorrowerButton.text =
                                items.lastName.plus(" " + items.firstName + " " + items.middleName)
                        }
                        presenter.deposit.borrowerId = (items.id!!)
                    }else{
                        if (items.fullName != null){
                            depositPledgeeButton.text = items.fullName
                        }else{
                            depositPledgeeButton.text =
                                items.lastName.plus(" " + items.firstName + " " + items.middleName)
                        }
                        presenter.deposit.pledgeeId = (items.id!!)
                    }

                }

            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        depositBorrowerButton.isEnabled = true
        depositPledgeeButton.isEnabled = true
    }

    override fun resetError() {
        presenter.validateError = false
        baseResetError(fields)
    }

    override fun showValidateError(error: Boolean, idName: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields, idName)
        }
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
                depositFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

}