package kz.putinbyte.iszhfermer.ui.offline.detail

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_offline_owners_detail.*
import kotlinx.android.synthetic.main.item_date_current.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.extensions.showToast
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.offline.detail.OfflineOwnersDetailPresenter
import kz.putinbyte.iszhfermer.presentation.offline.detail.OfflineOwnersDetailView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.offline.detail.rv.OfflineOwnersDetailAdapter
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class OfflineOwnersDetailFragment : BaseFragment(), OfflineOwnersDetailView {

    override val layoutResId = R.layout.fragment_offline_owners_detail

    companion object {

        fun newInstance() = OfflineOwnersDetailFragment()

        private var curOwner: Owners? = null

        fun newInstance(owners: Owners) = OfflineOwnersDetailFragment().apply {
            arguments = Bundle().apply {
                curOwner = owners
            }
        }
    }

    @ProvidePresenter
    fun providePresenter(): OfflineOwnersDetailPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(OfflineOwnersDetailPresenter::class.java)
            .apply {
                curOwner?.let {
                    currentOwner = it
                }
            }
    }

    lateinit var fields: List<FieldData>

    @InjectPresenter
    lateinit var detailPresenter: OfflineOwnersDetailPresenter

    var adapter = OfflineOwnersDetailAdapter(this::onItemClick)
    private var modelSelectedElements = ArrayList<BaseFormat>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainView).setTitle(getString(R.string.title_detail_owners))

        setListeners()

        fields = listOf(
            FieldData("Date", ownersDetailDateEdit, FieldType.DATE)
        )
    }

    private fun setListeners() {

        val builder = setAsteriskMark()
        asteriskDetailOwners.text = builder

        ownersDetailDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        detailOwnersSaveButton.setOnClickListener {

            activity?.showToast(modelSelectedElements.toString())

            detailPresenter.onSaveClicked()
        }
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showAnimalKind(items: List<BaseFormat>) {
        adapter.items = items
        ownersAddListRec.layoutManager = LinearLayoutManager(context)
        ownersAddListRec.adapter = adapter
    }

    private fun onItemClick(items: List<BaseFormat>?, checked: Boolean, position: Int) {
        if (checked) {
            if (items != null) {
                modelSelectedElements.add(items[position])
            }
        } else {
            val removed = modelSelectedElements.indexOfFirst {
                it.code == items!![position].code
            }
            modelSelectedElements.removeAt(removed)
        }
    }


    override fun visibleReset(visible: Boolean) {

    }

    override fun showData(owners: Owners) {
        ownersDetailFullNameText.text = owners.fullName ?: owners.lastName + " " + owners.firstName + " " + owners.middleName
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
                ownersDetailDateEdit.itemDateEdit.text =
                    frontFormat.format(calendar.time)
                detailPresenter.currentOwner.clearDate = backFormat.format(calendar.time)
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                detailAddOwners,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

    override fun showValidateError(error: Boolean, fieldKey: String) {
        if (error) {
            detailPresenter.validateError = true
            baseValidateError(fields, fieldKey)
        }
    }

    override fun resetError() {
        detailPresenter.validateError = false
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