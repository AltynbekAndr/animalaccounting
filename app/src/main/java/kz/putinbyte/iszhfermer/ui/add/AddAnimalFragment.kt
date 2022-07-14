package kz.putinbyte.iszhfermer.ui.add

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.*
import android.text.InputFilter.AllCaps
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import kz.putinbyte.iszhfermer.component.CheckNetworkConnection
import kz.putinbyte.iszhfermer.component.NetworkAvailability
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.extensions.afterTextChanged
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.add.AddAnimalPresenter
import kz.putinbyte.iszhfermer.presentation.add.AddAnimalView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.add.owners.OwnersBottomSheetFragment
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*


class AddAnimalFragment : BaseFragment(), AddAnimalView {

    override val layoutResId = R.layout.fragment_add
    var isConnect: Boolean = false
    var isDisconnect: Boolean = false

    companion object {
        var ownerId: Int? = null
        var katoId: Int? = null
        fun newInstance() = AddAnimalFragment()
        const val OWNER_KEY = "ownerId"
        const val KATO_KEY = "katoId"
        const val ANIMAL_KEY = "animalId"

        fun newInstance(katoId: Int?, ownerId: Int?, animalId: Int?) = AddAnimalFragment().apply {
            arguments = Bundle().apply {
                if (katoId != null) putInt(KATO_KEY, katoId)
                if (ownerId != null) putInt(OWNER_KEY, ownerId)
                if (animalId != null) putInt(ANIMAL_KEY, animalId)
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: AddAnimalPresenter
    var animalKindId: Int? = null

    @ProvidePresenter
    fun providePresenter(): AddAnimalPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(AddAnimalPresenter::class.java).apply {
                ownerId = arguments?.getInt(OWNER_KEY)
                katoId = arguments?.getInt(KATO_KEY)
                animalId = arguments?.getInt(ANIMAL_KEY)
            }
    }

    lateinit var fields: List<FieldData>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_add_animals))

        setListeners()

        fields = listOf(
            FieldData("Inj", addInjNumberEdit, FieldType.INPUT),
            FieldData("BirthDate", addBirthDateEdit, FieldType.DATE),
            FieldData("Owner", addOwnerButton, FieldType.INPUT),
            FieldData("OwnerOffline", addOwnerOfflineButton, FieldType.INPUT),
            FieldData("AnimalKindS", addAnimalKindSpinner, FieldType.SPINNER),
            FieldData("CauseRegistry", addCauseRegistrySpinner, FieldType.SPINNER),
            FieldData("Country", addCountrySpinner, FieldType.SPINNER),
            FieldData("Kato", addKatoSpinner, FieldType.SPINNER),
            FieldData("Identification", addTypeIdentificationSpinner, FieldType.SPINNER),
            FieldData("Direction", addDirectionTypeSpinner, FieldType.SPINNER),
            FieldData("Mast", addMastTypeSpinner, FieldType.SPINNER),
        )
    }

    private fun setListeners() {

        val mGetContent: ActivityResultLauncher<String> = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, {
                    var res = presenter.onUploadFileClicked(uri, context,activity) //1

                }) {
                    presenter.onPermissionDenied(getString(R.string.app_audio_permission_denied))
                }

            }
        }

        CheckNetworkConnection.get(requireContext()).observe(viewLifecycleOwner) {
            isConnect = it == NetworkAvailability.CONNECTED
            isDisconnect = it == NetworkAvailability.DISCONNECTED
            presenter.isDisconnect = isDisconnect
            if (isConnect) {
                presenter.isConnect = isConnect
                addKatoSpinner.visibility = View.GONE
                addOwnerOfflineButton.visibility = View.GONE
                addOwnerButton.visibility = View.VISIBLE
            } else {
                addKatoSpinner.visibility = View.VISIBLE
                addOwnerButton.visibility = View.GONE
                addOwnerOfflineButton.visibility = View.VISIBLE
            }
            if (isDisconnect) {
                presenter.showKato()
            }
        }

        addSaveButton.setOnClickListener {
            presenter.onSaveClicked()
        }

        addUploadFileButton.setOnClickListener {
            withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    if (!Environment.isExternalStorageManager()) {
                        val permissionObj : PermissionRequest = PermissionRequest(requireContext(),activity)
                        permissionObj.getManageStoragePermission()
                    }
                } else {
                    mGetContent.launch("application/*")
                }

            }) {
                presenter.onPermissionDenied(getString(R.string.app_audio_permission_denied))
            }
        }

        addBirthDateEdit.itemDateButton.setOnClickListener {
            showDatePickerDialog(presenter.isChip, presenter.minDay, presenter.maxDay)
        }

        addGreenDateEdit.itemDateButton.setOnClickListener {
            showGreenDate()
        }

        addScannerButton.setOnClickListener {
            withPermission(android.Manifest.permission.CAMERA, {
                presenter.onScannerClicked()
            }, {
                presenter.onPermissionDenied(getString(R.string.app_camera_permission_denied))
            })
        }

        addOwnerButton.setOnClickListener {
            showAlertOwners()
        }

        addCauseRegistrySpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.causeId = addCauseRegistrySpinner.items[position].id!!
                    presenter.onCauseRegistryChanged(addCauseRegistrySpinner.items, position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        addCountrySpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.countryId = addCountrySpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addTypeIdentificationSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.identId =
                        addTypeIdentificationSpinner.items[position].id!!
                    presenter.onIdentityChanged(addTypeIdentificationSpinner.items[position].code!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addKatoSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.katoId = addKatoSpinner.items[position].id!!

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addMrsSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.animalKindId = addMrsSpinner.items[position].id!!
                    presenter.loadDirections()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addAnimalKindSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.animalKindId = addAnimalKindSpinner.items[position].id!!
                    presenter.loadDirections()
                    presenter.onKindChanged(addAnimalKindSpinner.items[position].code)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addGenderSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.genderId = addGenderSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addDirectionTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =

            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.directionId =
                        addDirectionTypeSpinner.items[position].id!!
                    presenter.loadMastAnimal()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        addMastTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.currentAnimal.mastId = addMastTypeSpinner.items[position].id!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        addInjNumberEdit.filters = arrayOf<InputFilter>(AllCaps())
        addInjMotherEdit.filters = arrayOf<InputFilter>(AllCaps())
        addInjNumberEdit.afterTextChanged {
            presenter.currentAnimal.inj = it.uppercase().filter { s -> !s.isWhitespace() }
        }
        addRadioNumberEdit.afterTextChanged { presenter.currentAnimal.radioTag = it }
        addInjMotherEdit.afterTextChanged {
            presenter.currentAnimal.motherInj = it.uppercase().filter { s -> !s.isWhitespace() }
        }
        addGreenBookEdit.afterTextChanged { presenter.animalBook.number = it }
        addGreenBookPageEdit.afterTextChanged { presenter.animalBook.pageNumber = it }
        addOwnerOfflineButton.afterTextChanged { presenter.onOwnerChanged(ownerINN = it) }

        asterisksFields()
        showGreenVisibility()

    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                clAddAnimalFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

    private fun asterisksFields() {
        val builder = setAsteriskMark()
        asteriskRegistrySpinner.text = builder
        asteriskAnimalKindSpinner.text = builder
        asteriskDirectionTypeSpinner.text = builder
        asteriskBreedTypeSpinner.text = builder
        asteriskIdentificationSpinner.text = builder
        asteriskInjNumberEdit.text = builder
        asteriskCountrySpinner.text = builder
        asteriskGenderSpinner.text = builder
        asteriskBirthDateEdit.text = builder
        asteriskOwnerButton.text = builder
        asteriskGreenDate.text = builder
        asteriskGreenBookNumber.text = builder
        asteriskGreenBookPage.text = builder
    }

    private fun showGreenVisibility() {
        addGreenCheck.setOnCheckedChangeListener { _, b ->
            addUploadFileButton.visibility = if (b) View.VISIBLE else View.INVISIBLE
            addGreenDateEdit.visibility = if (b) View.VISIBLE else View.GONE
            addGreenBookEdit.visibility = if (b) View.VISIBLE else View.GONE
            addGreenBookPageEdit.visibility = if (b) View.VISIBLE else View.GONE
        }
    }

    override fun visibleReset(visible: Boolean) {
        addCountrySpinner.visible(visible)
        addTypeIdentificationSpinner.visible(visible)
        addKatoSpinner.visible(visible)
        addGenderSpinner.visible(visible)
        addCauseRegistrySpinner.visible(visible)
        addAnimalKindSpinner.visible(visible)
        addDirectionTypeSpinner.visible(visible)
        addMastTypeSpinner.visible(visible)
    }

    override fun showCountryOrigin(result: List<BaseFormat>) {
        addCountrySpinner.visible(!result.isNullOrEmpty())
        addCountrySpinner.items = result
        result.firstOrNull { it.id == presenter.currentAnimal.countryId }?.let {
            addCountrySpinner.setSelection(it.nameRu)
        }

    }

    override fun showTypeIdentification(items: List<BaseFormat>) {
        addTypeIdentificationSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, "null", "Выберите тип", null))
        newList.addAll(items)
        addTypeIdentificationSpinner.items = newList
        items.firstOrNull { it.id == presenter.currentAnimal.identId }?.let {
            addTypeIdentificationSpinner.setSelection(it.nameRu)
        }
    }

    override fun showKato(items: List<BaseFormat>) {
        addKatoSpinner.visible(!items.isNullOrEmpty() && !isConnect)
        if (!isConnect) {
            val newList = arrayListOf<BaseFormat>()
            newList.add(BaseFormat(0, null, "Выберите регион", null))
            newList.addAll(items)
            addKatoSpinner.items = newList
            items.firstOrNull { it.id == presenter.currentAnimal.katoId }?.let {
                addKatoSpinner.setSelection(it.nameRu)
            }
        }
    }

    override fun showGenderAnimal(items: List<BaseFormat>) {
        addGenderSpinner.visible(!items.isNullOrEmpty())
        addGenderSpinner.items = items
        items.firstOrNull { it.id == presenter.currentAnimal.genderId }?.let {
            addGenderSpinner.setSelection(it.nameRu)
        }

    }

    override fun showCauseRegistration(items: List<BaseFormat>) {
        addCauseRegistrySpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите основание", null))
        newList.addAll(items)
        addCauseRegistrySpinner.items = newList
        items.firstOrNull { it.id == presenter.currentAnimal.causeId }?.let {
            addCauseRegistrySpinner.setSelection(it.nameRu)
        }
    }

    override fun showKindOfAnimal(items: List<BaseFormat>) {
        addAnimalKindSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите вид", null))
        newList.addAll(items)
        addAnimalKindSpinner.items = newList
        items.firstOrNull { it.id == presenter.currentAnimal.animalKindId }?.let {
            addAnimalKindSpinner.setSelection(it.nameRu)
        }

    }

    override fun showAllDirections(items: List<BaseFormat>) {
        addDirectionTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите направление", null))
        newList.addAll(items)
        addDirectionTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.currentAnimal.directionId }?.let {
            addDirectionTypeSpinner.setSelection(it.nameRu)
        }

    }

    override fun showMrsTypeSpinner(items: List<BaseFormat>) {
        addMrsSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(9, "Goats", "Козы", "Ешкилер"))
        newList.add(BaseFormat(10, "Sheeps", "Овцы", "Койлар"))
        newList.addAll(items)
        addMrsSpinner.items = newList
    }

    override fun showMastTypes(items: List<BaseFormat>) {
        addMastTypeSpinner.visible(!items.isNullOrEmpty())
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите породу", null))
        newList.addAll(items)
        addMastTypeSpinner.items = newList
        items.firstOrNull { it.id == presenter.currentAnimal.mastId }?.let {
            addMastTypeSpinner.setSelection(it.nameRu)
        }
    }

    override fun showDateOfBirthday(date: String?) {
        addBirthDateEdit.itemDateEdit.text = date
    }

    override fun showDatePickerDialog(isStart: Boolean, minDay: Int, maxDay: Int) {
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
                addBirthDateEdit.itemDateEdit.text =
                    frontFormat.format(calendar.time)
                presenter.currentAnimal.birthDate = backFormat.format(calendar.time)


            },
            mYear,
            mMonth,
            mDay
        )
        if (isStart) {
            calendar.add(Calendar.DATE, minDay)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.datePicker.maxDate =
                (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * maxDay) - (1000 * 60 * 60 * 24 * maxDay) - (1000 * 60 * 60 * 24 * maxDay) - (1000 * 60 * 60 * 24 * maxDay) - (1000 * 60 * 60 * 24 * maxDay) - (1000 * 60 * 60 * 24 * 17))
            datePickerDialog.show()
        } else {
            calendar.add(Calendar.DATE, minDay)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.datePicker.maxDate =
                (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * maxDay))
            datePickerDialog.show()
        }

    }

    private fun showGreenDate() {
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
                addGreenDateEdit.itemDateEdit.text =
                    frontFormat.format(calendar.time)
                presenter.currentAnimal.animalBook?.date = backFormat.format(calendar.time)
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }

    private fun showAlertOwners() {
        val bottomSheetDialogFragment =
            OwnersBottomSheetFragment(object : OwnersBottomSheetFragment.Listener {
                override fun setOnClick(item: Owners) {
                    if (item.fullName != null) {
                        addOwnerButton.text = item.fullName
                    } else {
                        addOwnerButton.text = item.fullName
                            ?: item.lastName.plus(" " + item.firstName + " " + item.middleName)
                    }
                    presenter.onOwnerChanged(ownerId = item.id)
                }

            })
        bottomSheetDialogFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetDialogFragment.tag
        )
    }

    override fun setLoadingState(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showAnimalData(animal: RegAnimal) {
        addInjNumberEdit.setText(animal.inj)
        addRadioNumberEdit.setText(animal.radioTag)
        addInjMotherEdit.setText(animal.motherInj)
        addGreenBookEdit.setText(presenter.animalBook.number)
        addBirthDateEdit.itemDateEdit.text = animal.birthDate
        addGreenBookPageEdit.setText(presenter.animalBook.pageNumber)
//        presenter.ownerInfo?.let {
//            addOwnerButton.text = it.fullName
//                ?: it.lastName.plus(" " + it.firstName + " " + it.middleName)
//        }
    }

    override fun setOwnerInn(ownerInn: Owners?) {
        addOwnerButton.text = ownerInn?.fullName
            ?: ownerInn?.lastName.plus(" " + ownerInn?.firstName + " " + ownerInn?.middleName)
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

    override fun showValidateError(error: Boolean, fieldKey: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields, fieldKey)
        }
    }

    override fun showMrsSpinner(b: Boolean) {
        addMrsSpinner.visible(b)
    }

//    // Скрыть поля при изменении Основание регистрации
//    override fun showVisibility(hide: Boolean,isHideble:Boolean) {
//        addAnimalKindSpinner.visible(!hide) // true / gone
////        addDirectionTypeSpinner.visible(isHideble)
////        addMastTypeSpinner.visible(isHideble)
//        addTypeIdentificationSpinner.visible(!hide)
//        addRadioNumberEdit.visible(!hide)
//        addInjMotherEdit.visible(!hide)
//        addBirthDateEdit.visible(!hide)
//        addOwnerButton.visible(!hide)
//        addCountrySpinner.visible(hide)
//        addGenderSpinner.visible(hide)
//    }

    override fun resetError() {
        presenter.validateError = false
        baseResetError(fields)
    }

    override fun showErrorMessage(message: String) {
        try {
            Snackbar.make(
                clAddAnimalFragment,
                message,
                Snackbar.LENGTH_LONG
            )
                .show()
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
        }
    }

}