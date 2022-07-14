package kz.putinbyte.iszhfermer.ui.unregister

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_deregister.*
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.animals.unregister.Unregister
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.extensions.afterTextChanged
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.deregister.UnregisterPresenter
import kz.putinbyte.iszhfermer.presentation.deregister.UnregisterView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.add.owners.OwnersBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.search.dialogRegions.RegionsBottomSheetFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class UnregisterFragment : BaseFragment(), UnregisterView {

    override val layoutResId = R.layout.fragment_deregister

    companion object {
        const val CURRENT_KEY = "animalId"
        const val CURRENT_KEY_INJ = "inj"

        fun newInstance() = UnregisterFragment()
        fun newInstance(animalId: ArrayList<Int>?, injs: ArrayList<String>?) =
            UnregisterFragment().apply {
                arguments = Bundle().apply {
                    animalId?.let {
                        putIntegerArrayList(CURRENT_KEY, animalId)
                        putStringArrayList(CURRENT_KEY_INJ, injs)
                    }
                }
            }
    }

    @InjectPresenter
    lateinit var presenter: UnregisterPresenter

    lateinit var fields: List<FieldData>

    @ProvidePresenter
    fun providePresenter(): UnregisterPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(UnregisterPresenter::class.java).apply {
                animalsId = arguments?.getIntegerArrayList(CURRENT_KEY)
                injs = arguments?.getStringArrayList(CURRENT_KEY_INJ)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_deregister))
        setClickListeners()

        fields = listOf(
            FieldData("date", unregisterDateEdit, FieldType.DATE),
            FieldData("causeComment", unregisterCauseCommentEdit, FieldType.INPUT),
            FieldData("newOwner", unregisterOwnerButton, FieldType.INPUT),
            FieldData("newKato", unregisterKatoButton, FieldType.INPUT),
            FieldData("cause", unregisterCauseSpinner, FieldType.SPINNER),
            FieldData("sickness", unregisterSicknessSpinner, FieldType.SPINNER)
        )
    }

    private fun setClickListeners() {

        searchOkButton.setOnClickListener {
            presenter.saveClicked()
        }

        unregisterDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }

        unregisterKatoButton.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertRegions()
            }, 500)
        }

        unregisterOwnerButton.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertOwners()
            }, 500)
        }

        unregisterCauseSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.unregister.cause = unregisterCauseSpinner.items[position].id!!
                    presenter.onCauseChanged(unregisterCauseSpinner.items[position].code)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        unregisterSicknessSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.unregister.sicknessId = unregisterSicknessSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        unregisterCommentEdit.afterTextChanged { presenter.unregister.comment = it }

        unregisterCauseCommentEdit.afterTextChanged { presenter.unregister.causeComment = it }
    }

    override fun setCauseList(items: List<BaseFormat>) {
        unregisterCauseSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите причину", null))
        newList.addAll(items)
        unregisterCauseSpinner.items = newList
        items.firstOrNull { it.id == presenter.unregister.cause }?.let {
            unregisterCauseSpinner.setSelection(it.nameRu)
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
                unRegisterFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

    override fun showData(unregister: Unregister) {

    }

    override fun showSicknessType(items: List<BaseFormat>) {
        unregisterSicknessSpinner.visible(!items.isNullOrEmpty())
        unregisterSicknessSpinner.items = items
        items.firstOrNull { it.id == presenter.unregister.sicknessId }?.let {
            unregisterSicknessSpinner.setSelection(it.nameRu)
        }
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

    override fun visibleReset(visible: Boolean) {

    }

    override fun showVisibility() {
        unregisterKatoButton.visible(presenter.iskato)
        unregisterOwnerButton.visible(presenter.isOwner)
        unregisterSicknessSpinner.visible(presenter.isSickness)
        unregisterCauseCommentEdit.visible(presenter.isCauseComment)
        unregisterDateEdit.visible(presenter.isDate)
        unregisterCommentEdit.visible(presenter.isCommentEdit)
       // unregisterChooseFileButton.visible(presenter.isChooseFile)
    }

    fun showDatePickerDialog(isStart: Boolean) {
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
                if (isStart) {
                    unregisterDateEdit.itemDateEdit.text =
                        frontFormat.format(calendar.time)
                    presenter.unregister.endDate = (backFormat.format(calendar.time))
                }
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun showAlertRegions() {
        val bottomSheetDialogFragment =
            RegionsBottomSheetFragment(object : RegionsBottomSheetFragment.Listener {
                override fun setOnClick(items: Region.AnimalAmountByKato) {
                    unregisterKatoButton.text = items.name
                    presenter.setKato(items.id)
                }
            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        unregisterKatoButton.isEnabled = true
    }


    private fun showAlertOwners() {
        val bottomSheetDialogFragment =
            OwnersBottomSheetFragment(object : OwnersBottomSheetFragment.Listener {
                override fun setOnClick(items: Owners) {
                    if (items.fullName != null)
                        unregisterOwnerButton.text = items.fullName
                    else {
                        unregisterOwnerButton.text =
                            items.lastName.plus(" " + items.firstName + " " + items.middleName)
                    }
                    presenter.unregister.newOwnerId = (items.id!!)
                }

            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        unregisterOwnerButton.isEnabled = true
    }

}