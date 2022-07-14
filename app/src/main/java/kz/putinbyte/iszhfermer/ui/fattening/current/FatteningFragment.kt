package kz.putinbyte.iszhfermer.ui.fattening.current

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
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_fattening.*
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
import kz.putinbyte.iszhfermer.presentation.fattening.current.FatteningPresenter
import kz.putinbyte.iszhfermer.presentation.fattening.current.FatteningView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.ui.search.dialogRegions.RegionsBottomSheetFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class FatteningFragment : BaseFragment(), FatteningView {

    override val layoutResId = R.layout.fragment_fattening

    companion object {
        const val CURRENT_FATTENING_KEY = "animalId"
        fun newInstance() = FatteningFragment()

        fun newInstance(animalId: Int) = FatteningFragment().apply {
            arguments = Bundle().apply {
                putInt(CURRENT_FATTENING_KEY, animalId)
            }
        }
    }

    @ProvidePresenter
    fun providePresenter(): FatteningPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(FatteningPresenter::class.java).apply {
                animalId = arguments?.getInt(CURRENT_FATTENING_KEY)
            }
    }

    @InjectPresenter
    lateinit var presenter: FatteningPresenter
    lateinit var fields: List<FieldData>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_fattening))
        setListeners()

        fields = listOf(
            FieldData("Date", fatteningDateEdit, FieldType.DATE),
            FieldData("Area", fatteningSquareSpinner, FieldType.SPINNER)
        )
    }

    private fun setListeners() {

        fatteningSquareSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.fattening.fatteningAreaId =
                        fatteningSquareSpinner.items[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        fatteningCountrySpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.fattening.countryId =
                        fatteningCountrySpinner.items[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        ownerWeightAnimalEdit.afterTextChanged { presenter.fattening.animalWeight = (it).toInt() }

        fatteningImportSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.fattening.isAboardRK = isChecked
            presenter.onDisabled(isChecked)
        }

        fatteningFamilySwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.fattening.isFamilyFeeding = isChecked
        }

        fatteningSaveButton.setOnClickListener {
            presenter.onSaveClicked()
        }

        fatteningDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        fatteningMigrationButton.setOnClickListener {
            it.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                showAlertRegions()
            }, 500)
        }

        asterisksFields()

    }

    override fun showCountryType(list: List<BaseFormat>) {
        fatteningCountrySpinner.visible(!list.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(null, null, "Сделайте выбор", null))
        newList.addAll(list)
        fatteningCountrySpinner.items = newList
    }

    override fun showFatteningSquareType(list: List<BaseFormat>) {
        fatteningSquareSpinner.visible(!list.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите площадку", "Выберите площадку"))
        newList.addAll(list)
        fatteningSquareSpinner.items = newList
    }

    override fun visibleReset(visible: Boolean) {
        fatteningCountrySpinner.visible(visible)
        fatteningSquareSpinner.visible(visible)
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

    override fun disableKato() {
        fatteningMigrationButton.isEnabled = false
        fatteningMigrationButton.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.btn_primary_disabled,
            resources.newTheme()
        )
        presenter.fattening.migrationKatoId = null
    }

    override fun eneabled() {
        fatteningMigrationButton.isEnabled = true
        fatteningMigrationButton.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.bg_first_dropdown_text_view_text_v,
            resources.newTheme()
        )
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                fatteningFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
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

                val frontFormat = SimpleDateFormat(getString(R.string.app_front_date))

                fatteningDateEdit.itemDateEdit.text =
                    frontFormat.format(calendar.time)
                presenter.onDateChanged(frontFormat.format(calendar.time))
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun asterisksFields() {
        val builder = setAsteriskMark()
        asteriskSquare.text = builder
        asteriskDate.text = builder
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

    private fun showAlertRegions() {
        val bottomSheetDialogFragment =
            RegionsBottomSheetFragment(object : RegionsBottomSheetFragment.Listener {
                override fun setOnClick(items: Region.AnimalAmountByKato) {
                    fatteningMigrationButton.text = items.name
                    presenter.fattening.migrationKatoId = (items.id)
                }
            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
        fatteningMigrationButton.isEnabled = true
    }
}